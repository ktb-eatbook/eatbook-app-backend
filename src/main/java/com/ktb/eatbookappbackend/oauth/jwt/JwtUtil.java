package com.ktb.eatbookappbackend.oauth.jwt;

import static com.ktb.eatbookappbackend.oauth.jwt.constant.TokenType.ACCESS_TOKEN;
import static com.ktb.eatbookappbackend.oauth.jwt.constant.TokenType.REFRESH_TOKEN;

import com.ktb.eatbookappbackend.domain.member.exception.MemberException;
import com.ktb.eatbookappbackend.domain.member.message.MemberErrorCode;
import com.ktb.eatbookappbackend.domain.member.repository.MemberRepository;
import com.ktb.eatbookappbackend.domain.refreshToken.repository.RefreshTokenRepository;
import com.ktb.eatbookappbackend.entity.Member;
import com.ktb.eatbookappbackend.entity.RefreshToken;
import com.ktb.eatbookappbackend.entity.constant.Role;
import jakarta.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
@Slf4j
@Getter
public class JwtUtil {

    private static final String ROLE_CLAIM = "role";
    private static final String OAUTH_PROVIDER_CLAIM = "provider";
    private static final String MEMBER_ID_CLAIM = "memberId";
    private final RefreshTokenRepository refreshTokenRepository;
    private final MemberRepository memberRepository;

    @Value("${spring.security.jwt.access.expiration}")
    private long accessTokenExpirationPeriod;

    @Value("${spring.security.jwt.refresh.expiration}")
    private long refreshTokenExpirationPeriod;

    @Value("${spring.security.jwt.access.header}")
    private String accessHeader;

    @Value("${spring.security.jwt.refresh.header}")
    private String refreshHeader;

    @Value("${spring.security.jwt.secret-key}")
    private String secretCode;

    private SecretKey secretKey;

    public JwtUtil(RefreshTokenRepository refreshTokenRepository, MemberRepository memberRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.memberRepository = memberRepository;
    }

    @PostConstruct
    public void init() {
        secretKey = Keys.hmacShaKeyFor(secretCode.getBytes(StandardCharsets.UTF_8));
    }

    public String generateAccessToken(String memberId, Role role) {
        Date now = new Date();
        Date expireDate = new Date(now.getTime() + accessTokenExpirationPeriod);

        return Jwts.builder()
            .issuer("eatbook")
            .subject(ACCESS_TOKEN.getValue())
            .claim(MEMBER_ID_CLAIM, memberId)
            .claim(ROLE_CLAIM, role.name())
            .expiration(expireDate)
            .signWith(secretKey, Jwts.SIG.HS512)
            .compact();
    }

    public String generateRefreshToken(String memberId) {
        Date now = new Date();
        Date expireDate = new Date(now.getTime() + refreshTokenExpirationPeriod);
        String refreshToken = Jwts.builder()
            .issuer("eatbook")
            .subject(REFRESH_TOKEN.getValue())
            .claim(MEMBER_ID_CLAIM, memberId)
            .issuedAt(now)
            .expiration(expireDate)
            .signWith(secretKey, Jwts.SIG.HS512)
            .compact();
        saveRefreshToken(refreshToken, memberId);
        return refreshToken;
    }

    public void saveRefreshToken(String refreshToken, String memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));
        refreshTokenRepository.save(new RefreshToken(refreshToken, member));
    }

    private boolean isJwtValid(String token) {
        try {
            Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
            return true;
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT token입니다.");
        } catch (SignatureException | SecurityException | MalformedJwtException e) {
            log.error("유효하지 않은 JWT 서명입니다.");
        } catch (UnsupportedJwtException e) {
            log.error("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.error("잘못된 JWT 토큰입니다.");
        }
        return false;
    }

    public boolean validateSignupToken(String token) {
        return isJwtValid(token);
    }

    public boolean validateAccessToken(String token) {
        return isJwtValid(token);
    }

    public boolean validateRefreshToken(String token) {
        if (!isJwtValid(token)) {
            return false;
        }

        boolean existsInDb = refreshTokenRepository.findById(token).isPresent();
        if (!existsInDb) {
            log.error("Refresh token이 데이터베이스에 존재하지 않습니다.");
        }
        return existsInDb;
    }

    public JwtClaimDTO extractClaims(String token) {
        Claims payload = Jwts.parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)
            .getPayload();

        String memberId = payload.get(MEMBER_ID_CLAIM, String.class);
        Role userRole = Role.valueOf(payload.get(ROLE_CLAIM, String.class));

        return JwtClaimDTO.of(memberId, userRole);
    }

    public String generateSignupToken(String email, String nickname, String profileImage) {
        Date now = new Date();
        Date expireDate = new Date(now.getTime() + accessTokenExpirationPeriod);

        return Jwts.builder()
            .issuer("eatbook")
            .subject("signup-token")
            .claim("email", email)
            .claim("nickname", nickname)
            .claim("profileImage", profileImage)
            .expiration(expireDate)
            .signWith(secretKey, SignatureAlgorithm.HS512)
            .compact();
    }

    public Map<String, String> extractSignupClaims(String token) {
        Claims payload = Jwts.parser()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(token)
            .getBody();

        String email = payload.get("email", String.class);
        String nickname = payload.get("nickname", String.class);
        String profileImage = payload.get("profileImage", String.class);

        Map<String, String> claims = new HashMap<>();
        claims.put("email", email);
        claims.put("nickname", nickname);
        claims.put("profileImage", profileImage);
        return claims;
    }
}
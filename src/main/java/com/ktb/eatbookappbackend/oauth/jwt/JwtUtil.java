package com.ktb.eatbookappbackend.oauth.jwt;

import static com.ktb.eatbookappbackend.oauth.jwt.constant.TokenType.ACCESS_TOKEN;
import static com.ktb.eatbookappbackend.oauth.jwt.constant.TokenType.REFRESH_TOKEN;

import com.ktb.eatbookappbackend.entity.constant.Role;
import jakarta.annotation.PostConstruct;
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
    private static final String USER_ID_CLAIM = "userId";

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
            .claim(USER_ID_CLAIM, memberId)
            .claim(ROLE_CLAIM, role.name())
            .expiration(expireDate)
            .signWith(secretKey, Jwts.SIG.HS512)
            .compact();
    }

    public String generateRefreshToken(String memberId) {
        Date now = new Date();
        Date expireDate = new Date(now.getTime() + refreshTokenExpirationPeriod);

        return Jwts.builder()
            .issuer("eatbook")
            .subject(REFRESH_TOKEN.getValue())
            .claim(USER_ID_CLAIM, memberId)
            .issuedAt(now)
            .expiration(expireDate)
            .signWith(secretKey, Jwts.SIG.HS512)
            .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
            return true;
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT token 입니다.");
        } catch (SecurityException | MalformedJwtException e) {
            log.error("유효하지 않는 JWT 서명 입니다.");
        } catch (UnsupportedJwtException e) {
            log.error("지원되지 않는 JWT 토큰 입니다.");
        } catch (IllegalArgumentException e) {
            log.error("잘못된 JWT 토큰 입니다.");
        }
        return false;
    }

    public JwtClaimDTO extractClaims(String token) {
        Claims payload = Jwts.parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)
            .getPayload();

        String memberId = payload.get(USER_ID_CLAIM, String.class);
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

        return Map.of(
            "email", email,
            "nickname", nickname,
            "profileImage", profileImage
        );
    }
}
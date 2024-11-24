package com.ktb.eatbookappbackend.oauth.jwt;

import static com.ktb.eatbookappbackend.oauth.jwt.constant.TokenType.ACCESS_TOKEN;
import static org.springframework.http.HttpHeaders.SET_COOKIE;

import com.ktb.eatbookappbackend.domain.member.repository.MemberRepository;
import com.ktb.eatbookappbackend.entity.constant.Role;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TokenService {

    //    private final RefreshTokenRepository refreshTokenRepository;
    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;
    private final CookieService cookieService;

    public String renewToken(HttpServletResponse response, String refreshToken) {
        String renewAccessToken = createAccessToken(refreshToken);
        ResponseCookie accessCookie = cookieService.createCookie(ACCESS_TOKEN.getValue(), renewAccessToken);
        response.addHeader(SET_COOKIE, accessCookie.toString());
        return accessCookie.getValue();
    }

    public String createAccessToken(final String refreshToken) {
//        RefreshToken findRefreshToken = refreshTokenRepository.findById(refreshToken)
//            .orElseThrow(() -> new EntityNotFoundException("Refresh Token not found"));
//
//        Member member = memberRepository.findById(findRefreshToken.())
//            .orElseThrow(() -> new EntityNotFoundException("User not found"));

//        return jwtUtil.generateAccessToken(findRefreshToken.getUserId(), Role.MEMBER);
        return jwtUtil.generateAccessToken("0d512372-c241-4220-a3b4-1333a4d13479", Role.MEMBER);
    }

//    public void saveRefreshToken(String refreshToken, Long userId) {
//        refreshTokenRepository.save(new RefreshToken(refreshToken, userId));
//    }

//    public void deleteRefreshToken(String refreshToken) {
//        refreshTokenRepository.deleteById(refreshToken);
//    }
}

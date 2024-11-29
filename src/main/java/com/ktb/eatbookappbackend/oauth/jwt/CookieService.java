package com.ktb.eatbookappbackend.oauth.jwt;

import static com.ktb.eatbookappbackend.oauth.jwt.constant.TokenType.ACCESS_TOKEN;
import static com.ktb.eatbookappbackend.oauth.jwt.constant.TokenType.REFRESH_TOKEN;
import static org.springframework.http.HttpHeaders.SET_COOKIE;

import com.ktb.eatbookappbackend.domain.refreshToken.repository.RefreshTokenRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CookieService {

    @Value("${app.properties.cookieSameSite}")
    private String cookieSameSite;

    @Value("${app.properties.cookieSecure}")
    private boolean cookieSecure;

    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    public ResponseCookie createCookie(String cookieName, String cookieValue) {
        return ResponseCookie.from(cookieName, cookieValue)
            .path("/")
            .sameSite(cookieSameSite)
            .secure(cookieSecure)
            .httpOnly(false)
            .build();
    }

    public void deleteCookie(HttpServletResponse response, String cookieName) {
        ResponseCookie deleteCookie = ResponseCookie.from(cookieName, "")
            .path("/")
            .maxAge(0)
            .build();

        response.addHeader(SET_COOKIE, deleteCookie.toString());
    }

    public String extractCookie(Cookie[] cookies, String cookieName) {
        return Arrays.stream(cookies)
            .filter(cookie -> cookie.getName().equals(cookieName))
            .findFirst()
            .map(Cookie::getValue)
            .orElse(null);
    }

    public void clearAuthCookies(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();

        String accessToken = extractCookie(cookies, ACCESS_TOKEN.getValue());
        String refreshToken = extractCookie(cookies, REFRESH_TOKEN.getValue());

        if (accessToken != null) {
            deleteCookie(response, ACCESS_TOKEN.getValue());
        }

        if (refreshToken != null && jwtUtil.validateRefreshToken(refreshToken)) {
            refreshTokenRepository.deleteById(refreshToken);
            deleteCookie(response, REFRESH_TOKEN.getValue());
        }
    }
}
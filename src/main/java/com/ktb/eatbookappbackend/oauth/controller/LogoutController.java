package com.ktb.eatbookappbackend.oauth.controller;

import static com.ktb.eatbookappbackend.oauth.jwt.constant.TokenType.ACCESS_TOKEN;
import static com.ktb.eatbookappbackend.oauth.jwt.constant.TokenType.REFRESH_TOKEN;

import com.ktb.eatbookappbackend.domain.refreshToken.repository.RefreshTokenRepository;
import com.ktb.eatbookappbackend.global.reponse.SuccessResponse;
import com.ktb.eatbookappbackend.global.reponse.SuccessResponseDTO;
import com.ktb.eatbookappbackend.oauth.jwt.CookieService;
import com.ktb.eatbookappbackend.oauth.jwt.JwtUtil;
import com.ktb.eatbookappbackend.oauth.message.AuthSuccessCode;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/logout")
public class LogoutController {

    private final CookieService cookieService;
    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    @DeleteMapping()
    public ResponseEntity<SuccessResponseDTO> logout(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();

        String accessToken = cookieService.extractCookie(cookies, ACCESS_TOKEN.getValue());
        String refreshToken = cookieService.extractCookie(cookies, REFRESH_TOKEN.getValue());

        if (accessToken != null) {
            cookieService.deleteCookie(response, ACCESS_TOKEN.getValue());
        }

        if (refreshToken != null && jwtUtil.validateRefreshToken(refreshToken)) {
            refreshTokenRepository.deleteById(refreshToken);
            cookieService.deleteCookie(response, REFRESH_TOKEN.getValue());
        }

        return SuccessResponse.toResponseEntity(AuthSuccessCode.LOGOUT_COMPLETED);
    }
}

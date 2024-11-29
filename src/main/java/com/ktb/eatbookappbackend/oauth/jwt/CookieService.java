package com.ktb.eatbookappbackend.oauth.jwt;

import static org.springframework.http.HttpHeaders.SET_COOKIE;

import jakarta.servlet.http.HttpServletResponse;
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
}
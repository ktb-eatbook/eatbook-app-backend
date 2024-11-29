package com.ktb.eatbookappbackend.oauth.jwt;

import static com.ktb.eatbookappbackend.oauth.jwt.constant.TokenType.ACCESS_TOKEN;
import static com.ktb.eatbookappbackend.oauth.jwt.constant.TokenType.REFRESH_TOKEN;

import com.ktb.eatbookappbackend.entity.constant.Role;
import com.ktb.eatbookappbackend.oauth.exception.TokenException;
import com.ktb.eatbookappbackend.oauth.message.TokenErrorCode;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final TokenService tokenService;
    private final CookieService cookieService;

    private static final List<String> EXCLUDED_PATHS = List.of(
        "/api/signup/additional-info",
        "/api/signup",
        "/favicon.ico"
    );

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        return EXCLUDED_PATHS.contains(path);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws IOException, ServletException {
        Cookie[] cookies = request.getCookies();
        String path = request.getRequestURI();
        log.info("doFilterInternal에 들어온 request의 Path: " + path);

        if (cookies == null) {
            log.info("쿠키가 비어있습니다.");
            filterChain.doFilter(request, response);
            return;
        }

        String accessToken = extractCookie(cookies, ACCESS_TOKEN.getValue());
        String refreshToken = extractCookie(cookies, REFRESH_TOKEN.getValue());
        log.info("accessToken: " + accessToken);
        log.info("refreshToken: " + refreshToken);

        // case1. accessToken이 유효한 경우
        if (accessToken != null && jwtUtil.validateToken(accessToken)) {
            log.info("case1");
            setAuthInSecurityContext(accessToken);
            filterChain.doFilter(request, response);
            return;
        }

        // case2. accessToken이 유효하지 않고 refreshToken이 유효하면 accessToken 갱신
        if (refreshToken != null && jwtUtil.validateToken(refreshToken)) {
            log.info("case2");
            accessToken = tokenService.renewToken(response, refreshToken);
            setAuthInSecurityContext(accessToken);
            filterChain.doFilter(request, response);
            return;
        }

        // case3. accessToken과 refreshToken이 모두 유효하지 않으면 에러 응답 반환
        if (accessToken != null) {
            log.info("case3");
            cookieService.deleteCookie(response, ACCESS_TOKEN.getValue());
        }

        if (!jwtUtil.validateToken(refreshToken)) {
            log.info("refresh token이 유효하지 않음.");
            cookieService.deleteCookie(response, REFRESH_TOKEN.getValue());
//            tokenService.deleteRefreshToken(refreshToken);
        }

        throw new TokenException(TokenErrorCode.INVALID_TOKEN);
    }

    private String extractCookie(Cookie[] cookies, String cookieName) {
        return Arrays.stream(cookies)
            .filter(cookie -> cookie.getName().equals(cookieName))
            .findFirst()
            .map(Cookie::getValue)
            .orElse(null); // 쿠키가 존재하지 않을 경우 null 반환
    }

    private void setAuthInSecurityContext(String accessToken) {
        JwtClaimDTO claimDTO = jwtUtil.extractClaims(accessToken);

        String memberId = claimDTO.memberId();
        Role role = claimDTO.role();

        Authentication authentication =
            new UsernamePasswordAuthenticationToken(memberId, null, List.of(role::name));

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
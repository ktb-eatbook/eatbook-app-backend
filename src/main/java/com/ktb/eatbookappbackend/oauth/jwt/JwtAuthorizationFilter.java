package com.ktb.eatbookappbackend.oauth.jwt;

import static com.ktb.eatbookappbackend.oauth.jwt.constant.TokenType.ACCESS_TOKEN;
import static com.ktb.eatbookappbackend.oauth.jwt.constant.TokenType.REFRESH_TOKEN;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ktb.eatbookappbackend.entity.constant.Role;
import com.ktb.eatbookappbackend.global.reponse.FailureResponseDTO;
import com.ktb.eatbookappbackend.oauth.message.TokenErrorCode;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;
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
        "/api/signup",
        "/api/email-login",
        "/favicon.ico",
        "/api/novel/.*",              // /api/novel/{novelId}
        "/api/novel/.*/episodes",     // /api/novel/{novelId}/episodes
        "/api/search/log",
        "/api/search/novels"
    );

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        return matchesExcludedPaths(path);
    }

    private boolean matchesExcludedPaths(String path) {
        return EXCLUDED_PATHS.stream()
            .anyMatch(pattern -> Pattern.matches(pattern.replace(".*", ".*"), path));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws IOException, ServletException {
        Cookie[] cookies = request.getCookies();
        String path = request.getRequestURI();
        log.info("doFilterInternal에 들어온 request의 Path: " + path);

        if (cookies == null) {
            log.info("쿠키가 비어있습니다.");
            sendErrorResponse(response);
            return;
        }

        String accessToken = cookieService.extractCookie(cookies, ACCESS_TOKEN.getValue());
        String refreshToken = cookieService.extractCookie(cookies, REFRESH_TOKEN.getValue());
        log.info("accessToken: " + accessToken);
        log.info("refreshToken: " + refreshToken);

        if (accessToken != null && jwtUtil.validateAccessToken(accessToken)) {
            log.info("accessToken이 유효합니다.");
            setAuthInSecurityContext(accessToken);
            filterChain.doFilter(request, response);
            return;
        }

        if (refreshToken != null && jwtUtil.validateRefreshToken(refreshToken)) {
            log.info("accessToken이 유효하지 않고 refreshToken이 유효합니다.");
            accessToken = tokenService.renewToken(response, refreshToken);
            setAuthInSecurityContext(accessToken);
            filterChain.doFilter(request, response);
            return;
        }

        if (accessToken != null) {
            log.info("accessToken이 모두 유효하지 않아 삭제합니다.");
            cookieService.deleteCookie(response, ACCESS_TOKEN.getValue());
        }

        if (refreshToken != null && !jwtUtil.validateRefreshToken(refreshToken)) {
            log.info("refreshToken이 유효하지 않아 삭제합니다.");
            cookieService.deleteCookie(response, REFRESH_TOKEN.getValue());
        }

        sendErrorResponse(response);
    }

    private void setAuthInSecurityContext(String accessToken) {
        JwtClaimDTO claimDTO = jwtUtil.extractClaims(accessToken);

        String memberId = claimDTO.memberId();
        Role role = claimDTO.role();

        Authentication authentication =
            new UsernamePasswordAuthenticationToken(memberId, null, List.of(role::name));

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private void sendErrorResponse(HttpServletResponse response) throws IOException {
        TokenErrorCode tokenErrorCode = TokenErrorCode.INVALID_TOKEN;
        log.info("TokenError 발생");

        response.setStatus(tokenErrorCode.getStatus().value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        FailureResponseDTO failureResponseDTO = FailureResponseDTO.of(tokenErrorCode);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResponse = objectMapper.writeValueAsString(failureResponseDTO);

        response.getWriter().write(jsonResponse);
    }
}
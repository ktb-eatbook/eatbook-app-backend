package com.ktb.eatbookappbackend.oauth;

import static com.ktb.eatbookappbackend.oauth.jwt.constant.TokenType.ACCESS_TOKEN;
import static com.ktb.eatbookappbackend.oauth.jwt.constant.TokenType.REFRESH_TOKEN;
import static org.springframework.http.HttpHeaders.SET_COOKIE;

import com.ktb.eatbookappbackend.oauth.jwt.CookieService;
import com.ktb.eatbookappbackend.oauth.jwt.JwtUtil;
import com.ktb.eatbookappbackend.oauth.jwt.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Value("${app.properties.frontendDomain}")
    private String frontendDomain;

    private final JwtUtil jwtUtil;
    private final CookieService cookieService;
    private final TokenService tokenService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        log.info("OAuth2 Login 성공!");

        try {
            OAuth2CustomMember oAuth2member = (OAuth2CustomMember) authentication.getPrincipal();

            // 신규 사용자 처리
            boolean isNewUser = oAuth2member.getMember() == null;
            if (isNewUser) {
                log.info("신규 사용자, 추가 정보 입력 페이지로 리다이렉트");
                OAuth2MemberInfo memberInfo = oAuth2member.getMemberInfo();

                String email = memberInfo.getEmail();
                String nickname = memberInfo.getNickname();
                String profileImage = memberInfo.getProfileImage();

                // 회원가입 토큰 생성
                String signupToken = jwtUtil.generateSignupToken(email, nickname, profileImage);

                // 리디렉션 URL 생성
                String redirectUrl = UriComponentsBuilder.fromUriString(frontendDomain + "/additional-info")
                    .queryParam("token", signupToken)
                    .build()
                    .toUriString();

                log.info("Redirect URL: {}", redirectUrl);

                response.sendRedirect(redirectUrl);
                return;
            }

            String accessToken = jwtUtil.generateAccessToken(oAuth2member.getMemberId(), oAuth2member.getMemberRole());
            String refreshToken = jwtUtil.generateRefreshToken(oAuth2member.getMemberId());
            log.info("생성한 access token: " + accessToken);
            log.info("생성한 refresh token: " + refreshToken);
            tokenService.saveRefreshToken(refreshToken, oAuth2member.getMemberId());

            ResponseCookie accessTokenCookie = cookieService.createCookie(ACCESS_TOKEN.getValue(), accessToken);
            ResponseCookie refreshTokenCookie = cookieService.createCookie(REFRESH_TOKEN.getValue(), refreshToken);

            response.addHeader(SET_COOKIE, accessTokenCookie.toString());
            response.addHeader(SET_COOKIE, refreshTokenCookie.toString());

            response.sendRedirect(frontendDomain);
        } catch (Exception e) {
            log.error("OAuth2 Login 성공 후 예외 발생 : {}", e.getMessage());
        }
    }
}
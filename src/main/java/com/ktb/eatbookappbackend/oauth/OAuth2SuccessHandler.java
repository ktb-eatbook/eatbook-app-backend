package com.ktb.eatbookappbackend.oauth;

import com.ktb.eatbookappbackend.global.util.AESUtil;
import com.ktb.eatbookappbackend.oauth.jwt.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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
    private final AESUtil aesUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        log.info("OAuth2 Login 성공!");

        try {
            OAuth2CustomMember oAuth2member = (OAuth2CustomMember) authentication.getPrincipal();

            // 신규 사용자 처리
            boolean isNewMember = oAuth2member.getMember() == null;
            if (isNewMember) {
                log.info("신규 사용자, 추가 정보 입력 페이지로 리다이렉트");
                OAuth2MemberInfo memberInfo = oAuth2member.getMemberInfo();

                String email = memberInfo.getEmail();
                String nickname = memberInfo.getNickname();
                String profileImage = memberInfo.getProfileImage();

                // 회원가입 과정에서 사용되는 토큰 생성
                String signupToken = jwtUtil.generateSignupToken(email, nickname, profileImage);

                String redirectUrl = UriComponentsBuilder.fromUriString(
                        frontendDomain + "/additional-info")
                    .queryParam("token", signupToken)
                    .build()
                    .toUriString();

                log.info("Redirect URL: {}", redirectUrl);
                response.sendRedirect(redirectUrl);
                return;
            }

            log.info("기존 사용자, 성공적으로 로그인");
            OAuth2MemberInfo memberInfo = oAuth2member.getMemberInfo();
            String email = memberInfo.getEmail();
            String encryptedEmail = aesUtil.encrypt(email);
            String redirectUrl = frontendDomain + "/email-login#" + encryptedEmail;
            log.info("Encrypted Email: {}", encryptedEmail);
            log.info("Redirect URL: {}", redirectUrl);
            response.sendRedirect(redirectUrl);
        } catch (Exception e) {
            log.error("OAuth2 Login 성공 후 예외 발생 : {}", e.getMessage());
        }
    }
}
package com.ktb.eatbookappbackend.oauth.service;

import com.ktb.eatbookappbackend.domain.member.repository.MemberRepository;
import com.ktb.eatbookappbackend.entity.Member;
import com.ktb.eatbookappbackend.entity.constant.Role;
import com.ktb.eatbookappbackend.oauth.dto.OAuth2CustomMember;
import com.ktb.eatbookappbackend.oauth.dto.OAuthAttributes;
import java.util.Collections;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2MemberService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final MemberRepository memberRepository;

    OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService = new DefaultOAuth2UserService();

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        log.info("OAuth2 인증 처리 시작 - 사용자 정보 로드 중 (CustomOAuth2UserService.loadUser())");

        OAuth2User oAuth2User = oAuth2UserService.loadUser(oAuth2UserRequest);
        Map<String, Object> attributes = oAuth2User.getAttributes();
        String userNameAttributeName = oAuth2UserRequest.getClientRegistration()
            .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
        OAuthAttributes extractAttributes = OAuthAttributes.of(userNameAttributeName, oAuth2User.getAttributes());
        String email = extractAttributes.getOAuth2MemberInfo().getEmail();
        Member member = memberRepository.findByEmail(email).orElse(null);

        boolean isNewMember = member == null;

        if (isNewMember) {
            log.info("신규 사용자가 OAuth2 로그인: {}", extractAttributes);
            return new OAuth2CustomMember(
                Collections.singleton(new SimpleGrantedAuthority(Role.MEMBER.toString())),
                attributes,
                extractAttributes.getNameAttributeKey(),
                null,
                extractAttributes.getOAuth2MemberInfo()
            );
        }

        log.info("기존 회원 OAuth2 로그인: {}", extractAttributes);
        return new OAuth2CustomMember(
            Collections.singleton(new SimpleGrantedAuthority(member.getRole().name())),
            attributes,
            extractAttributes.getNameAttributeKey(),
            member,
            extractAttributes.getOAuth2MemberInfo()
        );
    }
}

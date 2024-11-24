package com.ktb.eatbookappbackend.oauth;

import com.ktb.eatbookappbackend.entity.Member;
import java.util.Map;
import lombok.Getter;

@Getter
public class OAuthAttributes {

    private final String nameAttributeKey;
    private final OAuth2MemberInfo oAuth2MemberInfo;

    private OAuthAttributes(String nameAttributeKey, OAuth2MemberInfo oAuth2MemberInfo) {
        this.nameAttributeKey = nameAttributeKey;
        this.oAuth2MemberInfo = oAuth2MemberInfo;
    }

    public static OAuthAttributes of(
        String userNameAttributeName, Map<String, Object> attributes) {
        return ofKakao(userNameAttributeName, attributes);
    }

    private static OAuthAttributes ofKakao(String userNameAttributeName, Map<String, Object> attributes) {
        return new OAuthAttributes(userNameAttributeName, new KakaoMemberInfo(attributes));
    }

    public Member toEntity(OAuth2MemberInfo oAuth2MemberInfo) {
        return Member.builder()
            .nickname(oAuth2MemberInfo.getNickname())
            .email(oAuth2MemberInfo.getEmail())
            .profileImageUrl(oAuth2MemberInfo.getProfileImage())
            .build();
    }
}
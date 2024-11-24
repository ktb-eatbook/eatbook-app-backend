package com.ktb.eatbookappbackend.oauth;

import com.ktb.eatbookappbackend.entity.Member;
import com.ktb.eatbookappbackend.entity.constant.Role;
import java.util.Collection;
import java.util.Map;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

@Getter
public class OAuth2CustomMember extends DefaultOAuth2User {

    private final Member member;
    private final OAuth2MemberInfo memberInfo;

    public OAuth2CustomMember(Collection<? extends GrantedAuthority> authorities, Map<String, Object> attributes, String nameAttributeKey,
        Member member, OAuth2MemberInfo memberInfo) {
        super(authorities, attributes, nameAttributeKey);
        this.member = member;
        this.memberInfo = memberInfo;
    }

    public String getMemberId() {
        return member.getId();
    }

    public Role getMemberRole() {
        return member.getRole();
    }
}

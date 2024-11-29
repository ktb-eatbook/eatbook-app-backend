package com.ktb.eatbookappbackend.oauth.dto;

import java.util.Map;

public abstract class OAuth2MemberInfo {

    protected Map<String, Object> attributes;

    public OAuth2MemberInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public abstract String getId();

    public abstract String getNickname();

    public abstract String getEmail();

    public abstract String getProfileImage();
}


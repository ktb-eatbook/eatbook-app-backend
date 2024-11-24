package com.ktb.eatbookappbackend.oauth.jwt.constant;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum TokenType {
    ACCESS_TOKEN("AccessToken"),
    REFRESH_TOKEN("RefreshToken");

    private final String value;
}

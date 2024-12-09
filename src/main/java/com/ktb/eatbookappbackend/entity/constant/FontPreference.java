package com.ktb.eatbookappbackend.entity.constant;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum FontPreference {
    SUIT("Suit"),
    DONGLE("Dongle");

    private final String value;
}

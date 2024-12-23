package com.ktb.eatbookappbackend.entity.constant;

import com.ktb.eatbookappbackend.domain.member.exception.MemberException;
import com.ktb.eatbookappbackend.domain.member.message.MemberErrorCode;
import java.util.Arrays;

public enum AgeGroup {
    TEENS(10),
    TWENTIES(20),
    THIRTIES(30),
    FORTIES(40),
    FIFTIES(50),
    SIXTIES(60),
    SEVENTIES(70),
    EIGHTIES(80),
    NINETIES(90),
    HUNDREDS(100);

    private final int value;

    AgeGroup(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static AgeGroup fromValue(int value) {
        return Arrays.stream(AgeGroup.values())
            .filter(group -> group.value == value)
            .findFirst()
            .orElseThrow(() -> new MemberException(MemberErrorCode.AGE_GROUP_NOT_FOUND));
    }
}

package com.ktb.eatbookappbackend.oauth.dto;

import com.ktb.eatbookappbackend.entity.constant.AgeGroup;
import com.ktb.eatbookappbackend.entity.constant.Gender;
import java.util.Map;

public record SignupMemberDTO(
    String email,
    String nickname,
    String profileImageUrl,
    Gender gender,
    AgeGroup ageGroup
) {

    public static SignupMemberDTO of(Map<String, String> claims, Gender gender, AgeGroup ageGroup) {
        return new SignupMemberDTO(
            claims.get("email"),
            claims.get("nickname"),
            claims.get("profileImage"),
            gender,
            ageGroup
        );
    }
}

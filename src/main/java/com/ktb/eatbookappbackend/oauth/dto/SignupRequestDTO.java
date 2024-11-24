package com.ktb.eatbookappbackend.oauth.dto;

import com.ktb.eatbookappbackend.entity.constant.AgeGroup;
import com.ktb.eatbookappbackend.entity.constant.Gender;

public record SignupRequestDTO(
    String token,
    int ageGroup,  // 숫자 값으로 요청 (20, 30 등)
    Gender gender
) {

    public AgeGroup getAgeGroupEnum() {
        return AgeGroup.fromValue(ageGroup);
    }
}

package com.ktb.eatbookappbackend.oauth.dto;

import com.ktb.eatbookappbackend.entity.constant.AgeGroup;
import com.ktb.eatbookappbackend.entity.constant.Gender;

public record SignupRequestDTO(
    String token,
    int ageGroup,
    Gender gender
) {

    public AgeGroup getAgeGroupEnum() {
        return AgeGroup.fromValue(ageGroup);
    }
}

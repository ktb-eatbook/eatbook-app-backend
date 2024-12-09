package com.ktb.eatbookappbackend.oauth.dto;

public record SignupResponseDTO(MemberDTO member) {

    public static SignupResponseDTO of(MemberDTO member) {
        return new SignupResponseDTO(member);
    }
}

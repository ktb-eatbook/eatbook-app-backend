package com.ktb.eatbookappbackend.oauth.dto;

public record SignupResponseDTO(
    String id,
    String nickname,
    String profileImage,
    String email
) {

    public static SignupResponseDTO of(String id, String nickname, String profileImage, String email) {
        return new SignupResponseDTO(id, nickname, profileImage, email);
    }
}

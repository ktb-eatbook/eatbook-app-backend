package com.ktb.eatbookappbackend.domain.global.reponse;

public record FailureResponseDTO (
        int statusCode,
        String message,
        String subStatus
) {
    public static FailureResponseDTO of(int statusCode, String message) {
        return new FailureResponseDTO(statusCode, message, null);
    }

    public static FailureResponseDTO of(int statusCode, String message, String subStatus) {
        return new FailureResponseDTO(statusCode, message, subStatus);
    }
}

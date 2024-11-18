package com.ktb.eatbookappbackend.global.reponse;

import com.ktb.eatbookappbackend.global.message.MessageCode;

public record FailureResponseDTO(
    int statusCode,
    String message,
    String subStatus
) {

    public static FailureResponseDTO of(MessageCode errorMessage) {
        return new FailureResponseDTO(errorMessage.getStatus().value(), errorMessage.getMessage(), null);
    }

    public static FailureResponseDTO of(int statusCode, String message) {
        return new FailureResponseDTO(statusCode, message, null);
    }

    public static FailureResponseDTO of(int statusCode, String message, String subStatus) {
        return new FailureResponseDTO(statusCode, message, subStatus);
    }
}

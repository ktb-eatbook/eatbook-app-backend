package com.ktb.eatbookappbackend.domain.global.reponse;

import com.ktb.eatbookappbackend.domain.global.message.MessageCode;

public record SuccessResponseDTO<T>(
    int statusCode,
    String message,
    T data
) {

    public static <T> SuccessResponseDTO<T> of(MessageCode successMessageCode) {
        return new SuccessResponseDTO<>(successMessageCode.getStatus().value(), successMessageCode.getMessage(), null);
    }

    public static <T> SuccessResponseDTO<T> of(MessageCode successMessageCode, T data) {
        return new SuccessResponseDTO<>(successMessageCode.getStatus().value(), successMessageCode.getMessage(), data);
    }
}

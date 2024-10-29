package com.ktb.eatbookappbackend.domain.global.reponse;

public record SuccessResponseDTO<T> (
        int statusCode,
        String message,
        T data
) {
    public static <T> SuccessResponseDTO<T> of(int statusCode, String message) {
        return new SuccessResponseDTO<>(statusCode, message, null);
    }

    public static <T> SuccessResponseDTO<T> of(int statusCode, String message, T data) {
        return new SuccessResponseDTO<>(statusCode, message, data);
    }
}

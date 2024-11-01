package com.ktb.eatbookappbackend.domain.global.reponse;

import com.ktb.eatbookappbackend.domain.global.message.MessageCode;
import java.util.Map;

public record SuccessResponseDTO<T>(
    int statusCode,
    String message,
    Map<String, Object> data
) {

    public static <T> SuccessResponseDTO<T> of(MessageCode successMessageCode) {
        return new SuccessResponseDTO<>(successMessageCode.getStatus().value(), successMessageCode.getMessage(), null);
    }

    public static <T> SuccessResponseDTO<T> of(MessageCode successMessageCode, Map<String, Object> data) {
        return new SuccessResponseDTO<>(successMessageCode.getStatus().value(), successMessageCode.getMessage(), data);
    }

    public static <T> SuccessResponseDTO<T> of(int statusCode, String message) {
        return new SuccessResponseDTO<>(statusCode, message, null);
    }

    public static <T> SuccessResponseDTO<T> of(int statusCode, String message, Map<String, Object> data) {
        return new SuccessResponseDTO<>(statusCode, message, data);
    }
}

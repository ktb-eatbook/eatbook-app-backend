package com.ktb.eatbookappbackend.global.reponse;

import com.ktb.eatbookappbackend.global.message.MessageCode;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

public class SuccessResponse {

    private SuccessResponse() {
    }

    public static ResponseEntity<SuccessResponseDTO> toResponseEntity(MessageCode successCode) {
        return ResponseEntity.status(successCode.getStatus())
            .body(SuccessResponseDTO.of(successCode));
    }

    public static <T> ResponseEntity<SuccessResponseDTO> toResponseEntity(MessageCode successCode, T data) {
        return ResponseEntity.status(successCode.getStatus())
            .body(SuccessResponseDTO.of(successCode, data));
    }

    public static <T> ResponseEntity<SuccessResponseDTO> toResponseEntity(
        MessageCode successCode,
        T data,
        HttpHeaders headers
    ) {
        return ResponseEntity.status(successCode.getStatus())
            .headers(headers)
            .body(SuccessResponseDTO.of(successCode, data));
    }
}
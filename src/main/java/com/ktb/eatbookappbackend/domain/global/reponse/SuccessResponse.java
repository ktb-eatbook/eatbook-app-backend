package com.ktb.eatbookappbackend.domain.global.reponse;

import com.ktb.eatbookappbackend.domain.global.message.MessageCode;
import org.springframework.http.ResponseEntity;

public class SuccessResponse {

    private SuccessResponse() {
    }

    public static ResponseEntity<?> toResponseEntity(MessageCode successCode) {
        return ResponseEntity.status(successCode.getStatus())
            .body(SuccessResponseDTO.of(successCode));
    }
}
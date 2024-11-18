package com.ktb.eatbookappbackend.global.reponse;

import com.ktb.eatbookappbackend.global.message.MessageCode;
import org.springframework.http.ResponseEntity;

public class SuccessResponse {

    private SuccessResponse() {
    }

    public static ResponseEntity<SuccessResponseDTO> toResponseEntity(MessageCode successCode) {
        return ResponseEntity.status(successCode.getStatus())
            .body(SuccessResponseDTO.of(successCode));
    }
}
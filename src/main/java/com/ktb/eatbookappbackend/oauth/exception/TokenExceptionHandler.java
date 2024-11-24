package com.ktb.eatbookappbackend.oauth.exception;

import com.ktb.eatbookappbackend.global.reponse.FailureResponseDTO;
import com.ktb.eatbookappbackend.oauth.message.TokenErrorCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class TokenExceptionHandler {

    @ExceptionHandler(TokenException.class)
    public ResponseEntity<FailureResponseDTO> handleTokenException(TokenException e) {
        TokenErrorCode errorCode = e.getErrorCode();
        return ResponseEntity.status(errorCode.getStatus())
            .body(FailureResponseDTO.of(errorCode));
    }
}

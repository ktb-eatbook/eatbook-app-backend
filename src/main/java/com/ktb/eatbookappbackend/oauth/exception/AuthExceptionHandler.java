package com.ktb.eatbookappbackend.oauth.exception;

import com.ktb.eatbookappbackend.global.reponse.FailureResponseDTO;
import com.ktb.eatbookappbackend.oauth.message.AuthErrorCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AuthExceptionHandler {

    @ExceptionHandler(SignupException.class)
    public ResponseEntity<FailureResponseDTO> handleSignupException(SignupException e) {
        AuthErrorCode errorCode = e.getErrorCode();
        return ResponseEntity.status(errorCode.getStatus())
            .body(FailureResponseDTO.of(errorCode));
    }
}

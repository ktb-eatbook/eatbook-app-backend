package com.ktb.eatbookappbackend.oauth.exception;

import com.ktb.eatbookappbackend.global.reponse.FailureResponseDTO;
import com.ktb.eatbookappbackend.oauth.message.SignupErrorCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class SignupExceptionHandler {

    @ExceptionHandler(SignupException.class)
    public ResponseEntity<FailureResponseDTO> handleSignupException(SignupException e) {
        SignupErrorCode errorCode = e.getErrorCode();
        return ResponseEntity.status(errorCode.getStatus())
            .body(FailureResponseDTO.of(errorCode));
    }
}

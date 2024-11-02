package com.ktb.eatbookappbackend.domain.global.exception;

import com.ktb.eatbookappbackend.domain.global.message.GlobalErrorMessage;
import com.ktb.eatbookappbackend.domain.global.reponse.FailureResponseDTO;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
        MethodArgumentTypeMismatchException.class,
        MissingServletRequestParameterException.class,
        ConstraintViolationException.class,
        MethodArgumentNotValidException.class,
        BindException.class,
        MethodArgumentTypeMismatchException.class})
    protected ResponseEntity<FailureResponseDTO> handleValidationExceptions(Exception e) {
        return ResponseEntity.badRequest()
            .body(FailureResponseDTO.of(GlobalErrorMessage.INVALID_QUERY_PARAMETER));
    }
}

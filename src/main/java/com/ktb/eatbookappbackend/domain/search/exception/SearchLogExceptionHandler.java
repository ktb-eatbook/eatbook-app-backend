package com.ktb.eatbookappbackend.domain.search.exception;

import com.ktb.eatbookappbackend.domain.global.reponse.FailureResponseDTO;
import com.ktb.eatbookappbackend.domain.search.message.SearchLogErrorCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class SearchLogExceptionHandler {

    @ExceptionHandler(SearchLogException.class)
    public ResponseEntity<?> handleNovelException(SearchLogException e) {
        SearchLogErrorCode errorCode = e.getErrorCode();
        return ResponseEntity.status(errorCode.getStatus())
            .body(FailureResponseDTO.of(errorCode));
    }
}

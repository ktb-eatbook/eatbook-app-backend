package com.ktb.eatbookappbackend.domain.novel.exception;

import com.ktb.eatbookappbackend.domain.novel.message.NovelFileErrorCode;
import com.ktb.eatbookappbackend.global.reponse.FailureResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class NovelFileExceptionHandler {

    @ExceptionHandler(NovelFileException.class)
    public ResponseEntity<FailureResponseDTO> handleNovelException(NovelFileException e) {
        NovelFileErrorCode errorCode = e.getErrorCode();
        return ResponseEntity.status(errorCode.getStatus())
            .body(FailureResponseDTO.of(errorCode));
    }
}


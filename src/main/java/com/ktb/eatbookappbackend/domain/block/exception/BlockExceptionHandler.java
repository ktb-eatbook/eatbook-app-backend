package com.ktb.eatbookappbackend.domain.block.exception;

import com.ktb.eatbookappbackend.domain.block.message.BlockErrorCode;
import com.ktb.eatbookappbackend.global.reponse.FailureResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class BlockExceptionHandler {

    @ExceptionHandler(BlockException.class)
    public ResponseEntity<FailureResponseDTO> handleNovelException(BlockException e) {
        BlockErrorCode errorCode = e.getErrorCode();
        return ResponseEntity.status(errorCode.getStatus())
            .body(FailureResponseDTO.of(errorCode));
    }
}


package com.ktb.eatbookappbackend.domain.episode.exception;

import com.ktb.eatbookappbackend.domain.episode.message.EpisodeErrorCode;
import com.ktb.eatbookappbackend.global.reponse.FailureResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class EpisodeExceptionHandler {

    @ExceptionHandler(EpisodeException.class)
    public ResponseEntity<FailureResponseDTO> handleNovelException(EpisodeException e) {
        EpisodeErrorCode errorCode = e.getErrorCode();
        return ResponseEntity.status(errorCode.getStatus())
            .body(FailureResponseDTO.of(errorCode));
    }
}


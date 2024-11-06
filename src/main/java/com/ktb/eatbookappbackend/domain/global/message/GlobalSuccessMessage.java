package com.ktb.eatbookappbackend.domain.global.message;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum GlobalSuccessMessage implements MessageCode {
    NO_RESULTS_FOUND("조회 결과가 없습니다.", HttpStatus.OK);

    private final String message;
    private final HttpStatus status;

    GlobalSuccessMessage(final String message, final HttpStatus status) {
        this.message = message;
        this.status = status;
    }
}

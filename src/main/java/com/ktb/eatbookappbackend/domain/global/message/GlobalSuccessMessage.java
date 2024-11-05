package com.ktb.eatbookappbackend.domain.global.message;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum GlobalSuccessMessage implements MessageCode {
    NO_RESULTS_FOUND("조회 결과가 없습니다.", HttpStatus.OK);

    private final String message;
    private final HttpStatus status;
}

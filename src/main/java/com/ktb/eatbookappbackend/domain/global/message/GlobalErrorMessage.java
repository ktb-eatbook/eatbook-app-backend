package com.ktb.eatbookappbackend.domain.global.message;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum GlobalErrorMessage implements MessageCode {
    INVALID_QUERY_PARAMETER("잘못된 쿼리 파라미터입니다.", HttpStatus.BAD_REQUEST),
    UNAUTHORIZED_ACCESS("인증되지 않은 요청입니다.", HttpStatus.UNAUTHORIZED);

    private final String message;
    private final HttpStatus status;
}
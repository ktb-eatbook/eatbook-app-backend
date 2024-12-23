package com.ktb.eatbookappbackend.global.message;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum GlobalErrorMessage implements MessageCode {
    INVALID_QUERY_PARAMETER("잘못된 쿼리 파라미터입니다.", HttpStatus.BAD_REQUEST),
    UNAUTHORIZED_ACCESS("인증되지 않은 요청입니다.", HttpStatus.UNAUTHORIZED),
    INVALID_PAGE_INDEX("유효하지 않은 페이지 번호입니다.", HttpStatus.BAD_REQUEST);

    private final String message;
    private final HttpStatus status;
}
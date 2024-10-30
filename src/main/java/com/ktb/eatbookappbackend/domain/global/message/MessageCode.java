package com.ktb.eatbookappbackend.domain.global.message;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public interface MessageCode {
    HttpStatus getStatus();
    String getMessage();

    @Getter
    enum GlobalErrorMessage implements MessageCode {
        INVALID_QUERY_PARAMETER("잘못된 쿼리 파라미터입니다.", HttpStatus.BAD_REQUEST),
        UNAUTHORIZED_ACCESS("인증되지 않은 요청입니다.", HttpStatus.UNAUTHORIZED);

        private final String message;
        private final HttpStatus status;

        GlobalErrorMessage(final String message, final HttpStatus status) {
            this.message = message;
            this.status = status;
        }
    }
}

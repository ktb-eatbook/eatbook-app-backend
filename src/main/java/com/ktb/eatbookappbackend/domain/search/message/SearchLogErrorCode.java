package com.ktb.eatbookappbackend.domain.search.message;

import com.ktb.eatbookappbackend.global.message.MessageCode;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum SearchLogErrorCode implements MessageCode {
    TERM_REQUIRED("검색어는 필수 입력 항목입니다.", HttpStatus.BAD_REQUEST);

    private final String message;
    private final HttpStatus status;
}

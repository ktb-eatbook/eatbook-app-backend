package com.ktb.eatbookappbackend.domain.search.message;

import com.ktb.eatbookappbackend.global.message.MessageCode;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum SearchLogSuccessCode implements MessageCode {
    SEARCH_LOG_CREATED("성공적으로 검색 기록을 생성했습니다.", HttpStatus.CREATED);

    private final String message;
    private final HttpStatus status;
}


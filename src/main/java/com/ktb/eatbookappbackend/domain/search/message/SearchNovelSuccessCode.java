package com.ktb.eatbookappbackend.domain.search.message;

import com.ktb.eatbookappbackend.global.message.MessageCode;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum SearchNovelSuccessCode implements MessageCode {
    SEARCH_SUCCESS("성공적으로 소설 목록을 검색했습니다.", HttpStatus.CREATED);

    private final String message;
    private final HttpStatus status;
}

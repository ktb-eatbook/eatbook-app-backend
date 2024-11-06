package com.ktb.eatbookappbackend.domain.readingLog.message;

import com.ktb.eatbookappbackend.domain.global.message.MessageCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ReadingLogSuccessCode implements MessageCode {
    BOOKMARKS_RETRIEVED("성공적으로 최근에 읽은 독서 기록을 조회했습니다.", HttpStatus.OK);

    private final String message;
    private final HttpStatus status;

    ReadingLogSuccessCode(final String message, final HttpStatus status) {
        this.message = message;
        this.status = status;
    }
}

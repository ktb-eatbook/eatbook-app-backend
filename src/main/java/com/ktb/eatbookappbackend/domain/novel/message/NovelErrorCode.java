package com.ktb.eatbookappbackend.domain.novel.message;

import com.ktb.eatbookappbackend.domain.global.message.MessageCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum NovelErrorCode implements MessageCode {
    NOVEL_NOT_FOUND("요청하신 소설을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    ALREADY_BOOKMARKED("이미 북마크된 소설입니다.", HttpStatus.CONFLICT),
    BOOKMARK_NOT_FOUND("삭제할 북마크를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);

    private final String message;
    private final HttpStatus status;

    NovelErrorCode(final String message, final HttpStatus status) {
        this.message = message;
        this.status = status;
    }
}

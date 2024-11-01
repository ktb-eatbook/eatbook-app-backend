package com.ktb.eatbookappbackend.domain.novel.message;

import com.ktb.eatbookappbackend.domain.global.message.MessageCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum NovelSuccessCode implements MessageCode {
    NOVEL_RETRIEVED("성공적으로 소설 정보를 조회했습니다.", HttpStatus.OK);

    private final String message;
    private final HttpStatus status;

    NovelSuccessCode(final String message, final HttpStatus status) {
        this.message = message;
        this.status = status;
    }
}

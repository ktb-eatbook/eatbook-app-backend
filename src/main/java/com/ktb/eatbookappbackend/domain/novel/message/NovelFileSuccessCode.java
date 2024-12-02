package com.ktb.eatbookappbackend.domain.novel.message;

import com.ktb.eatbookappbackend.global.message.MessageCode;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum NovelFileSuccessCode implements MessageCode {
    PRESIGNED_URL_RETRIEVED("Presigned URL을 성공적으로 조회했습니다.", HttpStatus.OK);

    private final String message;
    private final HttpStatus status;
}

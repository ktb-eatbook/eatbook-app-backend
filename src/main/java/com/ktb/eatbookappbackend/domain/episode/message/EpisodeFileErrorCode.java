package com.ktb.eatbookappbackend.domain.episode.message;

import com.ktb.eatbookappbackend.global.message.MessageCode;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum EpisodeFileErrorCode implements MessageCode {
    PRESIGNED_URL_GENERATION_FAILED("Presigned URL 생성 중 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    FILE_NOT_FOUND("요청하신 파일을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    FILE_TYPE_NOT_FOUND("지원되지 않는 파일 형식입니다.", HttpStatus.NOT_FOUND);

    private final String message;
    private final HttpStatus status;
}

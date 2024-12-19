package com.ktb.eatbookappbackend.domain.block.message;

import com.ktb.eatbookappbackend.global.message.MessageCode;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum BlockErrorCode implements MessageCode {
    ALREADY_BLOCKED("이미 차단된 유저입니다.", HttpStatus.CONFLICT);

    private final String message;
    private final HttpStatus status;
}

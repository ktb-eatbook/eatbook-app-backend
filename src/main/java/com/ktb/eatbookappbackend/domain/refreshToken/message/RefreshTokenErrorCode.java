package com.ktb.eatbookappbackend.domain.refreshToken.message;

import com.ktb.eatbookappbackend.global.message.MessageCode;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum RefreshTokenErrorCode implements MessageCode {
    TOKEN_NOT_FOUND("리프레시 토큰을 찾을 수 없습니다.", HttpStatus.NOT_FOUND);

    private final String message;
    private final HttpStatus status;
}

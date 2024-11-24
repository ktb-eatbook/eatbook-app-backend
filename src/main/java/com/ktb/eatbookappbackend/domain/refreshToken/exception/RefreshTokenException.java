package com.ktb.eatbookappbackend.domain.refreshToken.exception;

import com.ktb.eatbookappbackend.domain.refreshToken.message.RefreshTokenErrorCode;
import com.ktb.eatbookappbackend.global.exception.DomainException;
import lombok.Getter;

@Getter
public class RefreshTokenException extends DomainException {

    private final RefreshTokenErrorCode errorCode;

    public RefreshTokenException(RefreshTokenErrorCode errorCode) {
        super(errorCode);
        this.errorCode = errorCode;
    }
}

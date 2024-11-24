package com.ktb.eatbookappbackend.oauth.exception;

import com.ktb.eatbookappbackend.global.exception.DomainException;
import com.ktb.eatbookappbackend.oauth.message.TokenErrorCode;
import lombok.Getter;

@Getter
public class TokenException extends DomainException {

    private final TokenErrorCode errorCode;

    public TokenException(TokenErrorCode errorCode) {
        super(errorCode);
        this.errorCode = errorCode;
    }
}
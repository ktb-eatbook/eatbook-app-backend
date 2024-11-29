package com.ktb.eatbookappbackend.oauth.exception;

import com.ktb.eatbookappbackend.global.exception.DomainException;
import com.ktb.eatbookappbackend.oauth.message.AuthErrorCode;
import lombok.Getter;

@Getter
public class SignupException extends DomainException {

    private final AuthErrorCode errorCode;

    public SignupException(AuthErrorCode errorCode) {
        super(errorCode);
        this.errorCode = errorCode;
    }
}
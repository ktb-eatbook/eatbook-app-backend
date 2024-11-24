package com.ktb.eatbookappbackend.oauth.exception;

import com.ktb.eatbookappbackend.global.exception.DomainException;
import com.ktb.eatbookappbackend.oauth.message.SignupErrorCode;
import lombok.Getter;

@Getter
public class SignupException extends DomainException {

    private final SignupErrorCode errorCode;

    public SignupException(SignupErrorCode errorCode) {
        super(errorCode);
        this.errorCode = errorCode;
    }
}
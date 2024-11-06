package com.ktb.eatbookappbackend.domain.global.exception;

import com.ktb.eatbookappbackend.domain.global.message.GlobalErrorMessage;
import lombok.Getter;

@Getter
public class GlobalException extends DomainException {

    private final GlobalErrorMessage errorCode;

    public GlobalException(GlobalErrorMessage errorCode) {
        super(errorCode);
        this.errorCode = errorCode;
    }
}

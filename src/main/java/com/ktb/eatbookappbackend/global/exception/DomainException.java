package com.ktb.eatbookappbackend.global.exception;

import com.ktb.eatbookappbackend.global.message.MessageCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class DomainException extends RuntimeException {

    private final HttpStatus status;

    public DomainException(final MessageCode errorCode) {
        super(errorCode.getMessage());
        this.status = errorCode.getStatus();
    }
}

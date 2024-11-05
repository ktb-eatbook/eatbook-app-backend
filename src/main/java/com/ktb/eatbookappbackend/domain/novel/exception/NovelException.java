package com.ktb.eatbookappbackend.domain.novel.exception;

import com.ktb.eatbookappbackend.domain.global.exception.DomainException;
import com.ktb.eatbookappbackend.domain.novel.message.NovelErrorCode;
import lombok.Getter;

@Getter
public class NovelException extends DomainException {

    private final NovelErrorCode errorCode;

    public NovelException(NovelErrorCode errorCode) {
        super(errorCode);
        this.errorCode = errorCode;
    }
}
package com.ktb.eatbookappbackend.domain.novel.exception;

import com.ktb.eatbookappbackend.domain.global.exception.DomainException;
import com.ktb.eatbookappbackend.domain.novel.message.NovelErrorCode;

public class NovelException extends DomainException {

    public NovelException(NovelErrorCode errorCode) {
        super(errorCode);
    }
}
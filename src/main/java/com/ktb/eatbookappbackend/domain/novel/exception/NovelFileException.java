package com.ktb.eatbookappbackend.domain.novel.exception;

import com.ktb.eatbookappbackend.domain.novel.message.NovelFileErrorCode;
import com.ktb.eatbookappbackend.global.exception.DomainException;
import lombok.Getter;

@Getter
public class NovelFileException extends DomainException {

    private final NovelFileErrorCode errorCode;

    public NovelFileException(NovelFileErrorCode errorCode) {
        super(errorCode);
        this.errorCode = errorCode;
    }
}
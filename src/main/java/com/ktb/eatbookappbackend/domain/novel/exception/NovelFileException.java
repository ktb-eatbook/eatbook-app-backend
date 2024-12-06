package com.ktb.eatbookappbackend.domain.novel.exception;

import com.ktb.eatbookappbackend.domain.episode.message.EpisodeFileErrorCode;
import com.ktb.eatbookappbackend.global.exception.DomainException;
import lombok.Getter;

@Getter
public class NovelFileException extends DomainException {

    private final EpisodeFileErrorCode errorCode;

    public NovelFileException(EpisodeFileErrorCode errorCode) {
        super(errorCode);
        this.errorCode = errorCode;
    }
}
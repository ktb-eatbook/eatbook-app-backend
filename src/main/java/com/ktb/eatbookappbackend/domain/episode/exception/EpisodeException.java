package com.ktb.eatbookappbackend.domain.episode.exception;

import com.ktb.eatbookappbackend.domain.episode.message.EpisodeErrorCode;
import com.ktb.eatbookappbackend.global.exception.DomainException;
import lombok.Getter;

@Getter
public class EpisodeException extends DomainException {

    private final EpisodeErrorCode errorCode;

    public EpisodeException(EpisodeErrorCode errorCode) {
        super(errorCode);
        this.errorCode = errorCode;
    }
}

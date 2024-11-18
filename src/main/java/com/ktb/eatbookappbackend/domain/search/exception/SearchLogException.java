package com.ktb.eatbookappbackend.domain.search.exception;

import com.ktb.eatbookappbackend.global.exception.DomainException;
import com.ktb.eatbookappbackend.domain.search.message.SearchLogErrorCode;
import lombok.Getter;

@Getter
public class SearchLogException extends DomainException {

    private final SearchLogErrorCode errorCode;

    public SearchLogException(SearchLogErrorCode errorCode) {
        super(errorCode);
        this.errorCode = errorCode;
    }
}

package com.ktb.eatbookappbackend.domain.member.exception;

import com.ktb.eatbookappbackend.global.exception.DomainException;
import com.ktb.eatbookappbackend.domain.member.message.MemberErrorCode;
import lombok.Getter;

@Getter
public class MemberException extends DomainException {

    private final MemberErrorCode errorCode;

    public MemberException(MemberErrorCode errorCode) {
        super(errorCode);
        this.errorCode = errorCode;
    }
}
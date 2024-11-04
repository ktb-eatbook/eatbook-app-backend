package com.ktb.eatbookappbackend.domain.member.message;

import com.ktb.eatbookappbackend.domain.global.message.MessageCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum MemberErrorCode implements MessageCode {
    MEMBER_NOT_FOUND("요청하신 회원을 찾을 수 없습니다.", HttpStatus.NOT_FOUND);

    private final String message;
    private final HttpStatus status;

    MemberErrorCode(final String message, final HttpStatus status) {
        this.message = message;
        this.status = status;
    }
}

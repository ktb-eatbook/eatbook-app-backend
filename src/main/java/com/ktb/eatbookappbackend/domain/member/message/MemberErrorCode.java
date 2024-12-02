package com.ktb.eatbookappbackend.domain.member.message;

import com.ktb.eatbookappbackend.global.message.MessageCode;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum MemberErrorCode implements MessageCode {
    MEMBER_NOT_FOUND("요청하신 회원을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    AGE_GROUP_NOT_FOUND("잘못된 연령대입니다.", HttpStatus.BAD_REQUEST);

    private final String message;
    private final HttpStatus status;
}

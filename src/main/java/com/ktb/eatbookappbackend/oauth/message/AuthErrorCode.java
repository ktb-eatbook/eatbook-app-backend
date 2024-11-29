package com.ktb.eatbookappbackend.oauth.message;

import com.ktb.eatbookappbackend.global.message.MessageCode;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum AuthErrorCode implements MessageCode {

    EMAIL_DUPLICATED("이미 가입한 유저의 이메일입니다.", HttpStatus.CONFLICT);

    private final String message;
    private final HttpStatus status;
}

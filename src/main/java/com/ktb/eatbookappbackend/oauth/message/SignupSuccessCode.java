package com.ktb.eatbookappbackend.oauth.message;

import com.ktb.eatbookappbackend.global.message.MessageCode;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum SignupSuccessCode implements MessageCode {

    SIGN_UP_COMPLETED("성공적으로 회원가입했습니다.", HttpStatus.CREATED);

    private final String message;
    private final HttpStatus status;
}

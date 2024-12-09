package com.ktb.eatbookappbackend.oauth.message;

import com.ktb.eatbookappbackend.global.message.MessageCode;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum AuthErrorCode implements MessageCode {

    EMAIL_DUPLICATED("이미 가입한 유저의 이메일입니다.", HttpStatus.CONFLICT),
    ENCRYPTION_FAILED("데이터 암호화에 실패했습니다.", HttpStatus.BAD_REQUEST),
    DECRYPTION_FAILED("데이터 복호화에 실패했습니다.", HttpStatus.BAD_REQUEST),
    SIGNUP_TOKEN_INVALID("유효하지 않은 회원가입 토큰입니다.", HttpStatus.BAD_REQUEST);

    private final String message;
    private final HttpStatus status;
}

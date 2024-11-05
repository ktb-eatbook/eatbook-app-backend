package com.ktb.eatbookappbackend.domain.member.message;

import com.ktb.eatbookappbackend.domain.global.message.MessageCode;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum MemberSuccessCode implements MessageCode {
    BOOKMARKS_RETRIEVED("성공적으로 북마크를 누른 소설 목록을 조회했습니다.", HttpStatus.OK);

    private final String message;
    private final HttpStatus status;
}

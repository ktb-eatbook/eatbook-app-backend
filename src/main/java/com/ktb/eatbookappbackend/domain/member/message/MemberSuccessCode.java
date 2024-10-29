package com.ktb.eatbookappbackend.domain.member.message;

import com.ktb.eatbookappbackend.domain.global.message.MessageCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum MemberSuccessCode implements MessageCode {
    BOOKMARKS_RETRIEVED("성공적으로 북마크를 누른 소설 목록을 조회했습니다.", HttpStatus.OK),
    NO_RESULTS_FOUND("조회 결과가 없습니다.", HttpStatus.OK);

    private final String message;
    private final HttpStatus status;

    MemberSuccessCode(final String message, final HttpStatus status) {
        this.message = message;
        this.status = status;
    }
}

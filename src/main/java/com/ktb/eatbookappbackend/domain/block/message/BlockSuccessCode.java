package com.ktb.eatbookappbackend.domain.block.message;

import com.ktb.eatbookappbackend.global.message.MessageCode;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum BlockSuccessCode implements MessageCode {
    BLOCK_SUCCESS("성공적으로 차단했습니다.", HttpStatus.CREATED),
    BLOCKED_MEMBER_IDS_RETRIEVED("성공적으로 차단한 유저들의 ID 리스트를 조회했습니다.", HttpStatus.OK);

    private final String message;
    private final HttpStatus status;
}
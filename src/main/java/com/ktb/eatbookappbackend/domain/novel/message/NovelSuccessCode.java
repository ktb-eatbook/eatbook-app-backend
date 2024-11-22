package com.ktb.eatbookappbackend.domain.novel.message;

import com.ktb.eatbookappbackend.global.message.MessageCode;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum NovelSuccessCode implements MessageCode {
    NOVEL_RETRIEVED("성공적으로 소설 정보를 조회했습니다.", HttpStatus.OK),
    EPISODES_RETRIEVED("성공적으로 에피소드 목록을 조회했습니다.", HttpStatus.OK),
    BOOKMARK_ADDED("성공적으로 북마크를 생성했습니다.", HttpStatus.CREATED),
    BOOKMARK_DELETED("성공적으로 북마크를 삭제했습니다.", HttpStatus.OK),
    FAVORITE_ADDED("성공적으로 좋아요를 생성했습니다.", HttpStatus.CREATED),
    FAVORITE_DELETED("성공적으로 좋아요를 삭제했습니다.", HttpStatus.OK);

    private final String message;
    private final HttpStatus status;
}

package com.ktb.eatbookappbackend.domain.episode.message;

import com.ktb.eatbookappbackend.global.message.MessageCode;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum EpisodeSuccessCode implements MessageCode {
    COMMENTS_RETRIEVED("에피소드의 댓글 리스트를 성공적으로 조회했습니다.", HttpStatus.OK),
    COMMENT_CREATED("성공적으로 댓글을 생성했습니다.", HttpStatus.CREATED),
    COMMENT_DELETED("성공적으로 댓글을 삭제했습니다.", HttpStatus.OK);

    private final String message;
    private final HttpStatus status;
}
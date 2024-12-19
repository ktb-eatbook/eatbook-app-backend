package com.ktb.eatbookappbackend.domain.episode.message;

import com.ktb.eatbookappbackend.global.message.MessageCode;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum EpisodeErrorCode implements MessageCode {

    EPISODE_NOT_FOUND("에피소드를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    COMMENT_NOT_FOUND("삭제할 댓글을 찾지 못했습니다.", HttpStatus.NOT_FOUND),
    COMMENT_DELETE_PERMISSION_DENIED("해당 댓글을 삭제할 권한이 없습니다.", HttpStatus.UNAUTHORIZED);

    private final String message;
    private final HttpStatus status;
}



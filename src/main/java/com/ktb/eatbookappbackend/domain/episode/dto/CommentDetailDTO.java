package com.ktb.eatbookappbackend.domain.episode.dto;

import java.time.LocalDateTime;

public record CommentDetailDTO(
    String id,
    String content,
    String nickname,
    String profileImageUrl,
    LocalDateTime createdAt
) {

    public static CommentDetailDTO of(String id, String content, String nickname, String profileImageUrl, LocalDateTime createdAt) {
        return new CommentDetailDTO(id, content, nickname, profileImageUrl, createdAt);
    }
}

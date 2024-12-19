package com.ktb.eatbookappbackend.domain.episode.dto;

import java.time.LocalDateTime;

public record CommentDTO(
    String id,
    String content,
    String memberId,
    String nickname,
    String profileImageUrl,
    LocalDateTime createdAt
) {

    public static CommentDTO of(String id, String content, String memberId, String nickname, String profileImageUrl,
        LocalDateTime createdAt) {
        return new CommentDTO(id, content, memberId, nickname, profileImageUrl, createdAt);
    }
}

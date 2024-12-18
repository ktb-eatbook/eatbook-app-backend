package com.ktb.eatbookappbackend.domain.episode.dto;

import java.time.LocalDateTime;

public record CommentDTO(
    String id,
    String content,
    LocalDateTime createdAt
) {

    public static CommentDTO of(String id, String content, LocalDateTime createdAt) {
        return new CommentDTO(id, content, createdAt);
    }
}

package com.ktb.eatbookappbackend.domain.episode.dto;

import java.util.List;

public record EpisodeCommentsDTO(
    List<CommentDTO> comments
) {

    public static EpisodeCommentsDTO of(List<CommentDTO> comments) {
        return new EpisodeCommentsDTO(comments);
    }
}

package com.ktb.eatbookappbackend.domain.episode.dto;

import java.util.List;

public record EpisodeCommentsDTO(
    List<CommentDetailDTO> comments
) {

    public static EpisodeCommentsDTO of(List<CommentDetailDTO> comments) {
        return new EpisodeCommentsDTO(comments);
    }
}

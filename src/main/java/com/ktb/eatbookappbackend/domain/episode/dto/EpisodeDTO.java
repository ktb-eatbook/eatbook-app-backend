package com.ktb.eatbookappbackend.domain.episode.dto;

import com.ktb.eatbookappbackend.entity.Episode;

import java.time.LocalDateTime;

public record EpisodeDTO(
    String id,
    int chapterNumber,
    String title,
    LocalDateTime releasedDate,
    int viewCount,
    boolean ttsAvailable,
    boolean scriptAvailable,
    int commentCount
) {

    public static EpisodeDTO of(
        final Episode episode,
        final boolean ttsAvailable,
        final boolean scriptAvailable,
        final int commentCount
    ) {
        return new EpisodeDTO(
            episode.getId(),
            episode.getChapterNumber(),
            episode.getTitle(),
            episode.getReleasedDate(),
            episode.getViewCount(),
            ttsAvailable,
            scriptAvailable,
            commentCount
        );
    }
}

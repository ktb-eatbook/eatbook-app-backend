package com.ktb.eatbookappbackend.domain.novel.dto;

public record LastReadEpisodeDTO(
    String episodeId,
    int chapterNumber,
    String title,
    int pageNumber,
    String ttsLastPositionSeconds
) {

    public static LastReadEpisodeDTO of(
        String episodeId,
        int chapterNumber,
        String title,
        int pageNumber,
        String ttsLastPositionSeconds
    ) {
        return new LastReadEpisodeDTO(
            episodeId,
            chapterNumber,
            title,
            pageNumber,
            ttsLastPositionSeconds
        );
    }
}

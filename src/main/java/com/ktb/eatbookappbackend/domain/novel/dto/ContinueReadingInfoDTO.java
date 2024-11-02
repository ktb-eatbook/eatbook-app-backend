package com.ktb.eatbookappbackend.domain.novel.dto;

public record ContinueReadingInfoDTO(
    String episodeId,
    int chapterNumber,
    String title,
    int pageNumber,
    String ttsLastPositionSeconds
) {

    public static ContinueReadingInfoDTO of(
        String episodeId,
        int chapterNumber,
        String title,
        int pageNumber,
        String ttsLastPositionSeconds
    ) {
        return new ContinueReadingInfoDTO(
            episodeId,
            chapterNumber,
            title,
            pageNumber,
            ttsLastPositionSeconds
        );
    }
}

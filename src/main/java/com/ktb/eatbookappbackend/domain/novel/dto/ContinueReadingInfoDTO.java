package com.ktb.eatbookappbackend.domain.novel.dto;

public record ContinueReadingInfoDTO(
    LastReadEpisodeDTO continueReadingInfo
) {

    public static ContinueReadingInfoDTO of(LastReadEpisodeDTO continueReadingInfo) {
        return new ContinueReadingInfoDTO(continueReadingInfo);
    }
}

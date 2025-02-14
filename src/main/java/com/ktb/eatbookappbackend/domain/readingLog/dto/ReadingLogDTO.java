package com.ktb.eatbookappbackend.domain.readingLog.dto;

import com.ktb.eatbookappbackend.entity.Episode;
import com.ktb.eatbookappbackend.entity.Member;
import com.ktb.eatbookappbackend.entity.Novel;
import com.ktb.eatbookappbackend.entity.ReadingLog;

import java.sql.Time;

public record ReadingLogDTO (
        int pageNumber,
        Time ttsLastPositionSeconds,
        String novelId,
        String episodeId
) {
    public ReadingLog toEntity(final Member member, final Novel novel, final Episode episode) {
        return ReadingLog.builder()
                .pageNumber(this.pageNumber)
                .ttsLastPositionSeconds(this.ttsLastPositionSeconds)
                .member(member)
                .novel(novel)
                .episode(episode)
                .build();
    }
}

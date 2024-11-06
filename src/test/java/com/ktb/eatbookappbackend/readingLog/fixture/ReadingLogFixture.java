package com.ktb.eatbookappbackend.readingLog.fixture;

import com.ktb.eatbookappbackend.entity.Member;
import com.ktb.eatbookappbackend.entity.Novel;
import com.ktb.eatbookappbackend.entity.ReadingLog;
import com.ktb.eatbookappbackend.episode.fixture.EpisodeFixture;
import java.sql.Time;
import java.util.UUID;
import org.springframework.test.util.ReflectionTestUtils;

public class ReadingLogFixture {

    public static final int PAGE_NUMBER = 10;
    public static final Time TTS_LAST_POSITION_SECONDS = Time.valueOf("12:34:56");

    public static ReadingLog createReadingLog(Member member, Novel novel) {
        ReadingLog readingLog = ReadingLog.builder()
            .pageNumber(PAGE_NUMBER)
            .ttsLastPositionSeconds(TTS_LAST_POSITION_SECONDS)
            .member(member)
            .novel(novel)
            .episode(EpisodeFixture.createEpisode(novel))
            .build();

        String readingLogId = UUID.randomUUID().toString();
        ReflectionTestUtils.setField(readingLog, "id", readingLogId);
        return readingLog;
    }
}

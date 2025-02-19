package com.ktb.eatbookappbackend.readingLog.fixture;

import com.ktb.eatbookappbackend.domain.readingLog.dto.ReadingLogDTO;
import com.ktb.eatbookappbackend.entity.Episode;
import com.ktb.eatbookappbackend.entity.Member;
import com.ktb.eatbookappbackend.entity.Novel;
import com.ktb.eatbookappbackend.entity.ReadingLog;
import com.ktb.eatbookappbackend.episode.fixture.EpisodeFixture;
import java.sql.Time;
import java.util.UUID;
import org.springframework.test.util.ReflectionTestUtils;

public class ReadingLogFixture {

    public static final int EPISODE_ORDER = 1;
    public static final int SCRIPT_ORDER = 10;
    public static final Time LATES_DURATION = Time.valueOf("12:34:56");

    public static ReadingLogDTO createReadingLogDTO(Member member, Novel novel, Episode episode) {
        return new ReadingLogDTO(
                EPISODE_ORDER,
                SCRIPT_ORDER,
                LATES_DURATION,
                novel.getId(),
                episode.getId()
        );
    }
}

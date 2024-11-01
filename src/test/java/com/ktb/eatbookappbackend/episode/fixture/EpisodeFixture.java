package com.ktb.eatbookappbackend.episode.fixture;

import com.ktb.eatbookappbackend.entity.Episode;
import com.ktb.eatbookappbackend.entity.Novel;
import com.ktb.eatbookappbackend.entity.constant.EpisodeReleaseStatus;
import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.test.util.ReflectionTestUtils;

public class EpisodeFixture {

    public final static int EPISODE_COMMENTS_COUNT = 5;

    public static Episode createEpisode(Novel novel) {
        Episode episode = Episode.builder()
            .novel(novel)
            .title("Episode Title")
            .chapterNumber(1)
            .releasedDate(LocalDateTime.now())
            .releaseStatus(EpisodeReleaseStatus.PUBLIC)
            .build();
        String episodeID = UUID.randomUUID().toString();
        ReflectionTestUtils.setField(episode, "id", episodeID);
        return episode;
    }
}

package com.ktb.eatbookappbackend.episode.fixture;

import com.ktb.eatbookappbackend.entity.Episode;
import com.ktb.eatbookappbackend.entity.FileMetadata;
import com.ktb.eatbookappbackend.entity.Novel;
import com.ktb.eatbookappbackend.entity.constant.EpisodeReleaseStatus;
import com.ktb.eatbookappbackend.entity.constant.FileType;
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

    public static Episode createEpisode() {
        Episode episode = Episode.builder()
            .title("Episode Title")
            .chapterNumber(1)
            .releasedDate(LocalDateTime.now())
            .releaseStatus(EpisodeReleaseStatus.PUBLIC)
            .build();
        String episodeID = UUID.randomUUID().toString();
        ReflectionTestUtils.setField(episode, "id", episodeID);
        return episode;
    }

    public static FileMetadata createFileMetadata(FileType type, String path, Episode episode) {
        FileMetadata metadata = FileMetadata.builder()
            .type(type)
            .path(path)
            .episode(episode)
            .build();
        String fileMetadataId = UUID.randomUUID().toString();
        ReflectionTestUtils.setField(episode, "id", fileMetadataId);
        return metadata;
    }
}


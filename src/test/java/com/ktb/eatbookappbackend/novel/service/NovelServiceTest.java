package com.ktb.eatbookappbackend.novel.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ktb.eatbookappbackend.domain.comment.repository.CommentRepository;
import com.ktb.eatbookappbackend.domain.episode.dto.EpisodeDTO;
import com.ktb.eatbookappbackend.domain.favorite.repository.FavoriteRepository;
import com.ktb.eatbookappbackend.domain.fileMetaData.repository.FileMetaDataRepository;
import com.ktb.eatbookappbackend.domain.novel.dto.NovelDTO;
import com.ktb.eatbookappbackend.domain.novel.exception.NovelException;
import com.ktb.eatbookappbackend.domain.novel.message.NovelErrorCode;
import com.ktb.eatbookappbackend.domain.novel.repository.NovelRepository;
import com.ktb.eatbookappbackend.domain.novel.service.NovelService;
import com.ktb.eatbookappbackend.domain.redis.service.RedisService;
import com.ktb.eatbookappbackend.entity.Episode;
import com.ktb.eatbookappbackend.entity.Novel;
import com.ktb.eatbookappbackend.entity.constant.EpisodeSortOrder;
import com.ktb.eatbookappbackend.entity.constant.FileType;
import com.ktb.eatbookappbackend.episode.fixture.EpisodeFixture;
import com.ktb.eatbookappbackend.novel.fixture.NovelFixture;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

@ExtendWith(MockitoExtension.class)
public class NovelServiceTest {

    @Mock
    private NovelRepository novelRepository;

    @Mock
    private FavoriteRepository favoriteRepository;

    @Mock
    private FileMetaDataRepository fileMetaDataRepository;

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private NovelService novelService;

    @Mock
    private RedisService redisService;

    private Novel novel;

    @BeforeEach
    public void setUp() {
        novel = NovelFixture.createNovel();
    }

    @Test
    public void should_ReturnNovel_When_NovelExists() {
        // Given
        final String expectedNovelId = novel.getId();
        final String expectedNovelTitle = novel.getTitle();
        when(novelRepository.findById(novel.getId())).thenReturn(Optional.of(novel));

        // When
        Novel result = novelService.findById(expectedNovelId);

        // Then
        assertNotNull(result);
        assertEquals(expectedNovelId, result.getId());
        assertEquals(expectedNovelTitle, result.getTitle());
    }

    @Test
    public void should_ThrowException_When_NovelNotFound() {
        // Given
        final String expectedNovelId = novel.getId();
        when(novelRepository.findById(anyString())).thenReturn(Optional.empty());

        // Then
        NovelException exception = assertThrows(NovelException.class, () -> novelService.findById(expectedNovelId));
        assertEquals(NovelErrorCode.NOVEL_NOT_FOUND.getMessage(), exception.getMessage());
    }

    @Test
    public void should_ReturnNovelDTO_When_ValidNovelId() {
        // Given
        final String expectedNovelId = novel.getId();
        final String expectedNovelTitle = novel.getTitle();
        final int expectedFavoriteCount = NovelFixture.FAVORITE_COUNT;
        final int expectedViewCount = NovelFixture.VIEW_COUNT;
        when(novelRepository.findById(novel.getId())).thenReturn(Optional.of(novel));
        when(favoriteRepository.countByNovelId(novel.getId())).thenReturn(NovelFixture.FAVORITE_COUNT);
        when(redisService.incrementViewCount(expectedNovelId)).thenReturn(expectedViewCount);

        // When
        NovelDTO result = novelService.getNovel(expectedNovelId);

        // Then
        assertNotNull(result);
        assertEquals(expectedNovelId, result.id());
        assertEquals(expectedNovelTitle, result.title());
        assertEquals(expectedFavoriteCount, result.favoriteCount());
        verify(redisService, times(1)).incrementViewCount(expectedNovelId);
    }

    @Test
    public void should_ReturnEpisodes_When_NovelAndEpisodesExists() {
        // Given
        String expectedNovelId = novel.getId();
        Episode episode1 = EpisodeFixture.createEpisode(novel);
        Episode episode2 = EpisodeFixture.createEpisode(novel);
        List<Episode> expectedEpisodes = List.of(episode1, episode2);
        int commentCount = EpisodeFixture.EPISODE_COMMENTS_COUNT;

        when(novelRepository.findPublicEpisodesByNovelId(expectedNovelId, Sort.by(Direction.ASC, "releasedDate")))
            .thenReturn(expectedEpisodes);
        when(fileMetaDataRepository.existsByEpisodeIdAndType(episode1.getId(), FileType.TTS)).thenReturn(true);
        when(fileMetaDataRepository.existsByEpisodeIdAndType(episode1.getId(), FileType.SCRIPT)).thenReturn(false);
        when(fileMetaDataRepository.existsByEpisodeIdAndType(episode2.getId(), FileType.TTS)).thenReturn(false);
        when(fileMetaDataRepository.existsByEpisodeIdAndType(episode2.getId(), FileType.SCRIPT)).thenReturn(true);
        when(commentRepository.countCommentsByEpisodeId(episode1.getId())).thenReturn(commentCount);
        when(commentRepository.countCommentsByEpisodeId(episode2.getId())).thenReturn(commentCount);

        // When
        List<EpisodeDTO> result = novelService.getEpisodes(expectedNovelId, EpisodeSortOrder.oldest);

        // Then
        assertNotNull(result);
        assertEquals(expectedEpisodes.size(), result.size());

        EpisodeDTO episodeDto1 = result.get(0);
        assertEquals(episode1.getId(), episodeDto1.id());
        assertTrue(episodeDto1.ttsAvailable());
        assertFalse(episodeDto1.scriptAvailable());
        assertEquals(commentCount, episodeDto1.commentCount());

        EpisodeDTO episodeDto2 = result.get(1);
        assertEquals(episode2.getId(), episodeDto2.id());
        assertFalse(episodeDto2.ttsAvailable());
        assertTrue(episodeDto2.scriptAvailable());
        assertEquals(commentCount, episodeDto2.commentCount());
    }
}

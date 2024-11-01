package com.ktb.eatbookappbackend.novel.service;

import com.ktb.eatbookappbackend.domain.favorite.repository.FavoriteRepository;
import com.ktb.eatbookappbackend.domain.novel.dto.NovelDTO;
import com.ktb.eatbookappbackend.domain.novel.exception.NovelException;
import com.ktb.eatbookappbackend.domain.novel.message.NovelErrorCode;
import com.ktb.eatbookappbackend.domain.novel.repository.NovelRepository;
import com.ktb.eatbookappbackend.domain.novel.service.NovelService;
import com.ktb.eatbookappbackend.entity.Novel;
import com.ktb.eatbookappbackend.novel.fixture.NovelFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class NovelServiceTest {

    @Mock
    private NovelRepository novelRepository;

    @Mock
    private FavoriteRepository favoriteRepository;

    @InjectMocks
    private NovelService novelService;

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
        when(novelRepository.findById(novel.getId())).thenReturn(Optional.of(novel));
        when(favoriteRepository.countByNovelId(novel.getId())).thenReturn(NovelFixture.FAVORITE_COUNT);

        // When
        NovelDTO result = novelService.getNovel(expectedNovelId);

        // Then
        assertNotNull(result);
        assertEquals(expectedNovelId, result.id());
        assertEquals(expectedNovelTitle, result.title());
        assertEquals(expectedFavoriteCount, result.favoriteCount());
    }
}

package com.ktb.eatbookappbackend.search.service;

import static com.ktb.eatbookappbackend.search.fixture.SearchNovelFixture.NOVEL_FAVORITE_COUNT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ktb.eatbookappbackend.domain.favorite.dto.NovelFavoriteCountDTO;
import com.ktb.eatbookappbackend.domain.favorite.repository.FavoriteRepository;
import com.ktb.eatbookappbackend.domain.novel.repository.NovelRepository;
import com.ktb.eatbookappbackend.domain.search.dto.SearchNovelsResultDTO;
import com.ktb.eatbookappbackend.domain.search.service.SearchNovelService;
import com.ktb.eatbookappbackend.entity.Episode;
import com.ktb.eatbookappbackend.entity.Novel;
import com.ktb.eatbookappbackend.entity.constant.NovelSearchSortOrder;
import com.ktb.eatbookappbackend.episode.fixture.EpisodeFixture;
import com.ktb.eatbookappbackend.global.exception.GlobalException;
import com.ktb.eatbookappbackend.novel.fixture.NovelFixture;
import com.ktb.eatbookappbackend.search.fixture.SearchNovelFixture;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
public class SearchNovelServiceTest {

    @Mock
    private NovelRepository novelRepository;

    @Mock
    private FavoriteRepository favoriteRepository;

    @InjectMocks
    private SearchNovelService searchNovelService;

    private List<Novel> novels;

    private List<NovelFavoriteCountDTO> favoriteCounts;

    private final int totalPage = SearchNovelFixture.TOTAL_PAGE;
    private final int page = SearchNovelFixture.PAGE;
    private final int size = SearchNovelFixture.SIZE;

    @BeforeEach
    public void setUp() {
        novels = new ArrayList<>();
        List<String> titles = List.of("릴리귀환", "크리스탈귀환", "토니귀환", "전지적 에이든 시점", "전지적 강민 시점", "전지적 라빈 시점");
        LocalDateTime now = LocalDateTime.now();

        for (int i = 0; i < titles.size(); i++) {
            Novel novel = NovelFixture.createNovelWithTitle(titles.get(i));
            Episode episode = EpisodeFixture.createEpisode(novel);
            ReflectionTestUtils.setField(episode, "releasedDate", now.minusDays(i));

            novel.getEpisodes().add(episode);
            novels.add(novel);
        }

        favoriteCounts = novels.stream()
            .map(novel -> NovelFavoriteCountDTO.of(novel.getId(), NOVEL_FAVORITE_COUNT))
            .collect(Collectors.toList());
    }

    @Test
    public void should_ReturnSearchResultOrderedByRelevance_WithPaginationInfo_When_ValidInput() {
        // Given
        String searchTerm = "전지적";
        List<String> expectedTitles = List.of("전지적 에이든 시점", "전지적 강민 시점", "전지적 라빈 시점");
        NovelSearchSortOrder order = NovelSearchSortOrder.relevance;
        SearchNovelService spySearchNovelService = spy(searchNovelService);

        when(novelRepository.findAllWithDetails()).thenReturn(novels);

        when(favoriteRepository.findFavoriteCountsByNovelIds(anyList()))
            .thenReturn(favoriteCounts);

        // When
        SearchNovelsResultDTO result = spySearchNovelService.searchNovels(searchTerm, page, size, order);

        // Then
        verify(spySearchNovelService, times(1)).searchNovelsByRelevance(any(), any(), any());
        assertNotNull(result);
        assertEquals(page, result.pagination().currentPage());
        assertEquals(size, result.pagination().pageSize());
        assertEquals(totalPage, result.pagination().totalPages());
        assertEquals(novels.size(), result.novels().size());

        List<String> actualTitles = List.of(
            result.novels().get(0).title(),
            result.novels().get(1).title(),
            result.novels().get(2).title()
        );

        assertTrue(actualTitles.containsAll(expectedTitles), "전지적 키워드를 포함한 소설들의 우선순위가 높아야 합니다.");
    }

    @Test
    public void should_ReturnSearchResultOrderedByLatest_WithPaginationInfo_When_ValidInput() {
        // Given
        String searchTerm = "전지적";
        List<String> expectedTitles = List.of("릴리귀환", "크리스탈귀환", "토니귀환");
        NovelSearchSortOrder order = NovelSearchSortOrder.latest;
        SearchNovelService spySearchNovelService = spy(searchNovelService);

        when(novelRepository.findAllWithDetails()).thenReturn(novels);
        List<NovelFavoriteCountDTO> favoriteCounts = novels.stream()
            .map(novel -> NovelFavoriteCountDTO.of(novel.getId(), NOVEL_FAVORITE_COUNT))
            .collect(Collectors.toList());
        when(favoriteRepository.findFavoriteCountsByNovelIds(novels.stream().map(Novel::getId).collect(Collectors.toList())))
            .thenReturn(favoriteCounts);

        // When
        SearchNovelsResultDTO result = spySearchNovelService.searchNovels(searchTerm, page, size, order);

        // Then
        verify(spySearchNovelService, times(1)).searchNovelsByLatest(any(), any(), any());
        assertNotNull(result);
        assertEquals(page, result.pagination().currentPage());
        assertEquals(size, result.pagination().pageSize());
        assertEquals(totalPage, result.pagination().totalPages());
        assertEquals(novels.size(), result.novels().size());

        List<String> actualTitles = List.of(
            result.novels().get(0).title(),
            result.novels().get(1).title(),
            result.novels().get(2).title()
        );

        assertTrue(actualTitles.containsAll(expectedTitles), "에피소드의 릴리즈 날짜가 최신인 순으로 우선순위가 높아야 합니다.");
    }

    @Test
    public void should_ThrowException_When_InvalidPageIndex() {
        // Given
        String searchTerm = "귀환";
        int invalidPage = 10;
        NovelSearchSortOrder order = NovelSearchSortOrder.relevance;
        when(novelRepository.findAllWithDetails()).thenReturn(novels);

        // When & Then
        assertThrows(GlobalException.class, () -> searchNovelService.searchNovels(searchTerm, invalidPage, size, order));
    }

    @Test
    public void should_HandleEmptyNovelList() {
        // Given
        String searchTerm = "검색어";
        NovelSearchSortOrder order = NovelSearchSortOrder.relevance;
        List<Novel> emptyNovels = List.of();

        when(novelRepository.findAllWithDetails()).thenReturn(emptyNovels);

        // When
        SearchNovelsResultDTO result = searchNovelService.searchNovels(searchTerm, page, size, order);

        // Then
        assertNotNull(result);
        assertEquals(page, result.pagination().currentPage());
        assertEquals(size, result.pagination().pageSize());
        assertEquals(0, result.pagination().totalPages());
        assertEquals(0, result.novels().size());
    }
}
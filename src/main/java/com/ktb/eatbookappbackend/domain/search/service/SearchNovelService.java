package com.ktb.eatbookappbackend.domain.search.service;

import static com.ktb.eatbookappbackend.global.util.ValidationUtil.validatePageIndex;

import com.ktb.eatbookappbackend.domain.episode.repository.EpisodeRepository;
import com.ktb.eatbookappbackend.domain.favorite.repository.FavoriteRepository;
import com.ktb.eatbookappbackend.domain.novel.dto.NovelDTO;
import com.ktb.eatbookappbackend.domain.novel.repository.NovelRepository;
import com.ktb.eatbookappbackend.domain.search.dto.SearchNovelsResultDTO;
import com.ktb.eatbookappbackend.entity.Episode;
import com.ktb.eatbookappbackend.entity.Novel;
import com.ktb.eatbookappbackend.entity.constant.NovelSearchSortOrder;
import com.ktb.eatbookappbackend.global.dto.PaginationInfoDTO;
import com.ktb.eatbookappbackend.global.exception.GlobalException;
import com.ktb.eatbookappbackend.global.message.GlobalErrorMessage;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SearchNovelService {

    private static final int PAD_LENGTH = 255;
    private static final char PAD_CHAR = '#';

    private final NovelRepository novelRepository;
    private final FavoriteRepository favoriteRepository;
    private final EpisodeRepository episodeRepository;
    private final LevenshteinDistance levenshteinDistance = new LevenshteinDistance();

    public SearchNovelsResultDTO searchNovels(String searchTerm, int page, int size, NovelSearchSortOrder order) {
        int actualPageIndex = page - 1;
        List<Novel> novels = novelRepository.findAll();
        int totalPages = (int) Math.ceil((double) novels.size() / size);
        validatePageIndex(actualPageIndex, totalPages);
        PageRequest pageRequest = PageRequest.of(actualPageIndex, size);

        switch (order) {
            case relevance:
                return searchNovelsByRelevance(searchTerm, novels, pageRequest);
            case latest:
                return searchNovelsByLatest(searchTerm, novels, pageRequest);
            default:
                throw new GlobalException(GlobalErrorMessage.INVALID_QUERY_PARAMETER);
        }
    }

    public SearchNovelsResultDTO searchNovelsByRelevance(String searchTerm, List<Novel> novels, PageRequest pageRequest) {
        List<Novel> sortedNovels = novels.stream()
            .sorted((novel1, novel2) -> compareByRelevance(novel1, novel2, searchTerm))
            .collect(Collectors.toList());

        return createPaginatedResult(sortedNovels, pageRequest);
    }

    public SearchNovelsResultDTO searchNovelsByLatest(String searchTerm, List<Novel> novels, PageRequest pageRequest) {
        List<Novel> sortedNovels = novels.stream()
            .sorted((novel1, novel2) -> {
                int dateComparison = compareByLatestEpisode(novel1, novel2);
                if (dateComparison != 0) {
                    return dateComparison;
                }
                return compareByRelevance(novel1, novel2, searchTerm);
            })
            .collect(Collectors.toList());

        return createPaginatedResult(sortedNovels, pageRequest);
    }

    private int compareByLatestEpisode(Novel novel1, Novel novel2) {
        LocalDateTime latestDate1 = episodeRepository.findLatestEpisodesByNovel(novel1.getId()).stream()
            .findFirst()
            .map(Episode::getReleasedDate)
            .orElse(LocalDateTime.MIN);

        LocalDateTime latestDate2 = episodeRepository.findLatestEpisodesByNovel(novel2.getId()).stream()
            .findFirst()
            .map(Episode::getReleasedDate)
            .orElse(LocalDateTime.MIN);

        return latestDate2.compareTo(latestDate1);
    }

    private int compareByRelevance(Novel novel1, Novel novel2, String searchTerm) {
        String paddedTitle1 = padToFixedLength(novel1.getTitle(), PAD_LENGTH, PAD_CHAR);
        String paddedTitle2 = padToFixedLength(novel2.getTitle(), PAD_LENGTH, PAD_CHAR);

        int titleDistance1 = levenshteinDistance.apply(searchTerm, paddedTitle1);
        int titleDistance2 = levenshteinDistance.apply(searchTerm, paddedTitle2);

        if (titleDistance1 != titleDistance2) {
            return Integer.compare(titleDistance1, titleDistance2);
        }

        int authorDistance1 = novel1.getNovelAuthors().stream()
            .mapToInt(author -> levenshteinDistance.apply(searchTerm, author.getAuthor().getName()))
            .min()
            .orElse(Integer.MAX_VALUE);

        int authorDistance2 = novel2.getNovelAuthors().stream()
            .mapToInt(author -> levenshteinDistance.apply(searchTerm, author.getAuthor().getName()))
            .min()
            .orElse(Integer.MAX_VALUE);

        return Integer.compare(authorDistance1, authorDistance2);
    }

    private SearchNovelsResultDTO createPaginatedResult(List<Novel> sortedNovels, PageRequest pageRequest) {
        int start = (int) pageRequest.getOffset();
        int end = Math.min(start + pageRequest.getPageSize(), sortedNovels.size());
        List<Novel> paginatedNovels = sortedNovels.subList(start, end);

        PaginationInfoDTO paginationInfo = PaginationInfoDTO.of(
            pageRequest.getPageNumber() + 1,
            pageRequest.getPageSize(),
            sortedNovels.size(),
            (int) Math.ceil((double) sortedNovels.size() / pageRequest.getPageSize())
        );

        List<NovelDTO> novelDTOs = paginatedNovels.stream()
            .map(novel -> {
                int favoriteCount = favoriteRepository.countByNovelId(novel.getId());
                return NovelDTO.of(novel, favoriteCount);
            })
            .collect(Collectors.toList());

        return SearchNovelsResultDTO.of(paginationInfo, novelDTOs);
    }

    private String padToFixedLength(String input, int length, char paddingChar) {
        StringBuilder paddedString = new StringBuilder(input);
        while (paddedString.length() < length) {
            paddedString.append(paddingChar);
        }
        return paddedString.toString();
    }
}
package com.ktb.eatbookappbackend.domain.search.service;

import com.ktb.eatbookappbackend.domain.favorite.repository.FavoriteRepository;
import com.ktb.eatbookappbackend.global.dto.PaginationInfoDTO;
import com.ktb.eatbookappbackend.domain.novel.dto.NovelDTO;
import com.ktb.eatbookappbackend.domain.novel.repository.NovelRepository;
import com.ktb.eatbookappbackend.domain.search.dto.SearchNovelsResultDTO;
import com.ktb.eatbookappbackend.entity.Novel;
import com.ktb.eatbookappbackend.entity.constant.NovelSearchSortOrder;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SearchNovelService {

    private final NovelRepository novelRepository;
    private final FavoriteRepository favoriteRepository;
    private final LevenshteinDistance levenshteinDistance = new LevenshteinDistance();

    public SearchNovelsResultDTO searchNovels(String searchTerm, int page, int size, NovelSearchSortOrder order) {
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        return searchNovelsByRelevance(searchTerm, pageRequest);
    }

    public SearchNovelsResultDTO searchNovelsByRelevance(String searchTerm, PageRequest pageRequest) {
        List<Novel> novels = novelRepository.findAll();

        List<Novel> sortedNovels = novels.stream()
            .sorted((novel1, novel2) -> {
                String paddedTitle1 = padToFixedLength(novel1.getTitle(), 255, '#');
                String paddedTitle2 = padToFixedLength(novel2.getTitle(), 255, '#');

                int titleDistance1 = levenshteinDistance.apply(searchTerm, paddedTitle1);
                int titleDistance2 = levenshteinDistance.apply(searchTerm, paddedTitle2);

                if (titleDistance1 != titleDistance2) {
                    return Integer.compare(titleDistance1, titleDistance2);
                }

                // 제목 유사도가 같을 경우, 작가 이름으로 보조 정렬
                int authorDistance1 = novel1.getNovelAuthors().stream()
                    .mapToInt(author -> levenshteinDistance.apply(searchTerm, author.getAuthor().getName()))
                    .min()
                    .orElse(Integer.MAX_VALUE);
                int authorDistance2 = novel2.getNovelAuthors().stream()
                    .mapToInt(author -> levenshteinDistance.apply(searchTerm, author.getAuthor().getName()))
                    .min()
                    .orElse(Integer.MAX_VALUE);

                return Integer.compare(authorDistance1, authorDistance2);
            })
            .collect(Collectors.toList());

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
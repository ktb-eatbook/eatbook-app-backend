package com.ktb.eatbookappbackend.domain.search.service;

import static com.ktb.eatbookappbackend.global.util.ValidationUtil.validatePageIndex;

import com.ktb.eatbookappbackend.domain.favorite.dto.NovelFavoriteCountDTO;
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
import java.util.Map;
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
    private final LevenshteinDistance levenshteinDistance = new LevenshteinDistance();

    /**
     * 데이터베이스에서 모든 소설을 검색하고, 유효한 페이지 인덱스를 확인한 후 검색 기준에 따라 페이지네이션된 결과를 생성합니다.
     *
     * @param searchTerm 제목과 작가 이름에서 검색할 검색어입니다.
     * @param page       검색할 페이지 번호입니다. (1-기반 인덱스)
     * @param size       페이지당 결과 수입니다.
     * @param order      검색 결과의 정렬 순서입니다.
     * @return {@link SearchNovelsResultDTO}를 포함하는 {@link SearchNovelsResultDTO}로, 페이지네이션된 검색 결과와 페이지네이션 정보를 포함합니다.
     * @throws GlobalException 제공된 페이지 인덱스가 잘못된 경우 발생합니다.
     */
    public SearchNovelsResultDTO searchNovels(String searchTerm, int page, int size, NovelSearchSortOrder order) {
        int actualPageIndex = page - 1;
        List<Novel> novels = novelRepository.findAllWithDetails();
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

    /**
     * 소설 목록을 검색어와 관련도가 높은 순으로 정렬하고, 페이지네이션된 결과를 생성합니다.
     *
     * @param searchTerm  검색할 검색어로, 소설 제목과 작가 이름을 비교합니다.
     * @param novels      정렬할 소설 목록입니다.
     * @param pageRequest 결과에 대한 페이지네이션 정보입니다.
     * @return {@link SearchNovelsResultDTO}로, 페이지네이션된 검색 결과와 페이지네이션 정보를 포함합니다.
     */
    public SearchNovelsResultDTO searchNovelsByRelevance(String searchTerm, List<Novel> novels, PageRequest pageRequest) {
        List<Novel> sortedNovels = novels.stream()
            .sorted((novel1, novel2) -> compareByRelevance(novel1, novel2, searchTerm))
            .collect(Collectors.toList());

        return createPaginatedResult(sortedNovels, pageRequest);
    }

    /**
     * 소설 목록을 최신 에피소드의 릴리즈 날짜가 최신인 순으로 정렬하고, 날짜가 같으면 관련도가 높은 순으로 정렬합니다.
     *
     * @param searchTerm  소설 제목과 작가 이름을 비교할 검색어입니다.
     * @param novels      정렬할 소설 목록입니다.
     * @param pageRequest 결과에 대한 페이지네이션 정보입니다.
     * @return {@link SearchNovelsResultDTO}로, 페이지네이션된 검색 결과와 페이지네이션 정보를 포함합니다.
     */
    public SearchNovelsResultDTO searchNovelsByLatest(String searchTerm, List<Novel> novels, PageRequest pageRequest) {
        List<Novel> sortedNovels = novels.stream()
            .sorted((novel1, novel2) -> {
                int releasedDateComparison = compareByLatestEpisode(novel1, novel2);
                if (releasedDateComparison != 0) {
                    return releasedDateComparison;
                }
                return compareByRelevance(novel1, novel2, searchTerm);
            })
            .collect(Collectors.toList());

        return createPaginatedResult(sortedNovels, pageRequest);
    }

    /**
     * 두 소설의 최신 에피소드의 릴리즈 날짜를 비교합니다. 릴리즈 날짜가 더 최근인 소설이 더 높은 우선순위로 간주됩니다. 릴리스 날짜가 같으면, 검색어와의 관련성을 기반으로 정렬합니다.
     *
     * @param novel1 비교할 첫 번째 소설입니다.
     * @param novel2 비교할 두 번째 소설입니다.
     * @return 첫 번째 소설이 두 번째 소설보다 작으면 음수, 같으면 0, 크면 양수를 반환합니다.
     */
    private int compareByLatestEpisode(Novel novel1, Novel novel2) {
        LocalDateTime latestDate1 = novel1.getEpisodes().stream()
            .findFirst()
            .map(Episode::getReleasedDate)
            .orElse(LocalDateTime.MIN);

        LocalDateTime latestDate2 = novel2.getEpisodes().stream()
            .findFirst()
            .map(Episode::getReleasedDate)
            .orElse(LocalDateTime.MIN);

        return latestDate2.compareTo(latestDate1);
    }

    /**
     * 두 소설의 제목과 작가 이름과의 유사도를 비교하여, 검색어와의 관련성에 따라 정렬합니다.
     *
     * @param novel1     첫 번째 소설
     * @param novel2     두 번째 소설
     * @param searchTerm 검색어
     * @return 첫 번째 소설이 두 번째 소설보다 작으면 음수, 같으면 0, 크면 양수. 검색어와의 제목 유사도가 다르면, 제목 유사도에 따라 정렬합니다. 제목 유사도가 같으면, 작가 이름과의 유사도에 따라 정렬합니다.
     */
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

        List<String> novelIds = paginatedNovels.stream()
            .map(Novel::getId)
            .collect(Collectors.toList());

        List<NovelFavoriteCountDTO> favoriteCounts = favoriteRepository.findFavoriteCountsByNovelIds(novelIds);
        Map<String, Long> favoriteCountsMap = favoriteCounts.stream()
            .collect(Collectors.toMap(NovelFavoriteCountDTO::novelId, NovelFavoriteCountDTO::count));

        List<NovelDTO> novelDTOs = paginatedNovels.stream()
            .map(novel -> {
                int favoriteCount = favoriteCountsMap.getOrDefault(novel.getId(), 0L).intValue();
                return NovelDTO.of(novel, favoriteCount);
            })
            .collect(Collectors.toList());

        return SearchNovelsResultDTO.of(paginationInfo, novelDTOs);
    }

    /**
     * 지정된 길이에 도달할 때까지 입력 문자열을 지정된 패딩 문자로 채웁니다.
     *
     * @param input       패딩할 입력 문자열
     * @param length      패딩된 문자열의 원하는 길이
     * @param paddingChar 패딩에 사용할 문자
     * @return 패딩된 문자열. 입력 문자열이 지정된 길이보다 길면, 원래 문자열이 그대로 반환됩니다.
     */
    private String padToFixedLength(String input, int length, char paddingChar) {
        StringBuilder paddedString = new StringBuilder(input);
        while (paddedString.length() < length) {
            paddedString.append(paddingChar);
        }
        return paddedString.toString();
    }
}
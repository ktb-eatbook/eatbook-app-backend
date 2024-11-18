package com.ktb.eatbookappbackend.domain.search.controller;

import com.ktb.eatbookappbackend.global.reponse.SuccessResponseDTO;
import com.ktb.eatbookappbackend.domain.search.dto.SearchNovelsResultDTO;
import com.ktb.eatbookappbackend.domain.search.message.SearchNovelSuccessCode;
import com.ktb.eatbookappbackend.domain.search.service.SearchNovelService;
import com.ktb.eatbookappbackend.entity.constant.NovelSearchSortOrder;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/search/novels")
public class SearchNovelController {

    private final SearchNovelService searchNovelService;

    /**
     * 소설을 검색합니다.
     *
     * @param page 페이지 번호로, 페이지네이션에 사용됩니다. 1 이상의 정수여야 합니다.
     * @param size 페이지당 결과 수를 나타냅니다. 1 이상의 정수여야 합니다.
     * @param term 제목과 작가 이름에서 검색할 검색어입니다.
     * @param sort 검색 결과의 정렬 순서를 나타냅니다. 기본값은 'relevance'이며, 제공되지 않으면 기본값으로 설정됩니다.
     * @return 검색 결과를 포함하는 ResponseEntity<SuccessResponseDTO>를 반환합니다.
     */
    @GetMapping()
    public ResponseEntity<SuccessResponseDTO> createSearchLog(
        @RequestParam(name = "page") @Min(1) final int page,
        @RequestParam(name = "size") @Min(1) final int size,
        @RequestParam(name = "term") final String term,
        @RequestParam(name = "sort", required = false, defaultValue = "relevance") final NovelSearchSortOrder sort) {
        SearchNovelsResultDTO searchResult = searchNovelService.searchNovels(term, page, size, sort);
        return ResponseEntity.ok(SuccessResponseDTO.of(SearchNovelSuccessCode.SEARCH_SUCCESS, searchResult));
    }
}

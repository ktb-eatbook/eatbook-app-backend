package com.ktb.eatbookappbackend.domain.search.controller;

import com.ktb.eatbookappbackend.domain.global.reponse.SuccessResponseDTO;
import com.ktb.eatbookappbackend.domain.search.dto.SearchNovelsResultDTO;
import com.ktb.eatbookappbackend.domain.search.message.SearchNovelSuccessCode;
import com.ktb.eatbookappbackend.domain.search.service.SearchNovelService;
import com.ktb.eatbookappbackend.entity.constant.NovelSearchSortOrder;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/search/novels")
public class SearchNovelController {

    private final SearchNovelService searchNovelService;

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

package com.ktb.eatbookappbackend.domain.search.controller;

import com.ktb.eatbookappbackend.domain.global.reponse.SuccessResponseDTO;
import com.ktb.eatbookappbackend.domain.search.dto.SearchLogDTO;
import com.ktb.eatbookappbackend.domain.search.dto.SearchLogRequest;
import com.ktb.eatbookappbackend.domain.search.dto.SearchLogResponse;
import com.ktb.eatbookappbackend.domain.search.message.SearchLogSuccessCode;
import com.ktb.eatbookappbackend.domain.search.service.SearchLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/search/log")
public class SearchLogController {

    private final SearchLogService searchService;

    @PostMapping()
    public ResponseEntity<SuccessResponseDTO> createSearchLog(@RequestBody SearchLogRequest searchLogRequest) {
        SearchLogDTO searchLog = searchService.createSearchLog(searchLogRequest.term());
        return ResponseEntity.ok(SuccessResponseDTO.of(SearchLogSuccessCode.SEARCH_LOG_CREATED, SearchLogResponse.of(searchLog)));
    }
}

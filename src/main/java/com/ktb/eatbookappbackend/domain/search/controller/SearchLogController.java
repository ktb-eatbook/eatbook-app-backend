package com.ktb.eatbookappbackend.domain.search.controller;

import com.ktb.eatbookappbackend.global.reponse.SuccessResponseDTO;
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

    /**
     * 이 메소드는 검색 로그 생성 요청을 처리합니다. {@link SearchLogRequest} 객체를 받아 검색어를 포함하고, {@link SearchLogService}를 사용하여 해당 검색어에 해당하는
     * {@link SearchLogDTO} 객체를 생성합니다. 그런 다음, {@link ResponseEntity}를 반환하며, 성공 코드와 생성된 {@link SearchLogResponse}를 포함하는
     * {@link SuccessResponseDTO}를 담습니다.
     *
     * @param searchLogRequest 검색어를 포함하는 요청 객체.
     * @return {@link SearchLogResponse}를 포함하는 성공 코드가 있는 {@link SuccessResponseDTO}를 포함하는 {@link ResponseEntity}.
     */
    @PostMapping()
    public ResponseEntity<SuccessResponseDTO> createSearchLog(@RequestBody SearchLogRequest searchLogRequest) {
        SearchLogDTO searchLog = searchService.createSearchLog(searchLogRequest.term());
        return ResponseEntity.ok(SuccessResponseDTO.of(SearchLogSuccessCode.SEARCH_LOG_CREATED, SearchLogResponse.of(searchLog)));
    }
}

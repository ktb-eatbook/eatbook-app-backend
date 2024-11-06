package com.ktb.eatbookappbackend.domain.search.service;

import com.ktb.eatbookappbackend.domain.search.dto.SearchLogDTO;
import com.ktb.eatbookappbackend.domain.search.exception.SearchLogException;
import com.ktb.eatbookappbackend.domain.search.message.SearchLogErrorCode;
import com.ktb.eatbookappbackend.domain.search.repository.SearchLogRepository;
import com.ktb.eatbookappbackend.entity.SearchLog;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class SearchLogService {

    private final SearchLogRepository searchLogRepository;

    @Transactional
    public SearchLogDTO createSearchLog(String term) {
        if (term == null || term.trim().isEmpty()) {
            throw new SearchLogException(SearchLogErrorCode.TERM_REQUIRED);
        }

        SearchLog searchLog = SearchLog.builder()
            .term(term)
            .build();

        searchLogRepository.save(searchLog);
        return SearchLogDTO.of(searchLog.getId(), term);
    }
}

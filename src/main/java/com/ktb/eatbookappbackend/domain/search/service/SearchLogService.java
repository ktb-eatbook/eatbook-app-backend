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

    /**
     * 데이터베이스에 새로운 검색 로그 항목을 생성합니다.
     *
     * @param term 사용자가 입력한 검색어. 이 값은 null이거나 빈 문자열일 수 없습니다.
     * @return {@link SearchLogDTO} 객체로, 새로 생성된 검색 로그 항목의 ID와 검색어를 포함합니다.
     * @throws SearchLogException 검색어가 null이거나 빈 문자열인 경우 발생합니다.
     */
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

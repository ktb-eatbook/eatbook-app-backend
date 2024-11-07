package com.ktb.eatbookappbackend.search.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ktb.eatbookappbackend.domain.search.dto.SearchLogDTO;
import com.ktb.eatbookappbackend.domain.search.exception.SearchLogException;
import com.ktb.eatbookappbackend.domain.search.message.SearchLogErrorCode;
import com.ktb.eatbookappbackend.domain.search.repository.SearchLogRepository;
import com.ktb.eatbookappbackend.domain.search.service.SearchLogService;
import com.ktb.eatbookappbackend.entity.SearchLog;
import com.ktb.eatbookappbackend.search.fixture.SearchLogFixture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class SearchLogServiceTest {

    @InjectMocks
    private SearchLogService searchLogService;

    @Mock
    private SearchLogRepository searchLogRepository;

    @Test
    void should_CreateSearchLog_When_TermIsValid() {
        // Given
        String term = "검색어";
        SearchLog expectedSearchLog = SearchLogFixture.createSearchLog(term);
        when(searchLogRepository.save(any(SearchLog.class))).thenReturn(expectedSearchLog);

        // When
        SearchLogDTO result = searchLogService.createSearchLog(term);

        // Then
        verify(searchLogRepository).save(any(SearchLog.class));
        assertEquals(expectedSearchLog.getTerm(), result.term());
    }

    @Test
    void should_ThrowException_When_TermIsNull() {
        // Given
        String term = null;

        // When & Then
        SearchLogException exception = assertThrows(SearchLogException.class, () -> searchLogService.createSearchLog(term));
        assertEquals(SearchLogErrorCode.TERM_REQUIRED, exception.getErrorCode());
    }

    @Test
    void should_ThrowException_When_TermIsEmpty() {
        // Given
        String term = "   ";

        // When & Then
        SearchLogException exception = assertThrows(SearchLogException.class, () -> searchLogService.createSearchLog(term));
        assertEquals(SearchLogErrorCode.TERM_REQUIRED, exception.getErrorCode());
    }
}
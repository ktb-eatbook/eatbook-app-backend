package com.ktb.eatbookappbackend.search.fixture;

import com.ktb.eatbookappbackend.entity.SearchLog;
import java.util.UUID;
import org.springframework.test.util.ReflectionTestUtils;

public class SearchLogFixture {

    public static SearchLog createSearchLog(String term) {
        SearchLog searchLog = SearchLog.builder()
            .term(term)
            .build();
        String searchLogId = UUID.randomUUID().toString();
        ReflectionTestUtils.setField(searchLog, "id", searchLogId);
        return searchLog;
    }
}

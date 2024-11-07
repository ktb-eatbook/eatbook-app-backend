package com.ktb.eatbookappbackend.domain.search.dto;

public record SearchLogResponse(
    SearchLogDTO searchLog
) {

    public static SearchLogResponse of(SearchLogDTO searchLog) {
        return new SearchLogResponse(searchLog);
    }
}

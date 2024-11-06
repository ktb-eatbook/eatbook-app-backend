package com.ktb.eatbookappbackend.domain.search.dto;

public record SearchLogDTO(
    String id,
    String term
) {

    public static SearchLogDTO of(String id, String term) {
        return new SearchLogDTO(id, term);
    }
}

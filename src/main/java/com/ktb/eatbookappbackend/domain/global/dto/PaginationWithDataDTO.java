package com.ktb.eatbookappbackend.domain.global.dto;

import java.util.List;
import java.util.Map;

public record PaginationWithDataDTO<T>(
        PaginationInfoDTO pagination,
        Map<String, List<T>> data
) {
    public static <T> PaginationWithDataDTO<T> of(PaginationInfoDTO pagination, String key, List<T> items) {
        return new PaginationWithDataDTO<>(pagination, Map.of(key, items));
    }
}
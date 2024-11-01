package com.ktb.eatbookappbackend.domain.global.dto;

public record PaginationInfoDTO(
        int currentPage,
        int pageSize,
        int totalItems,
        int totalPages
) {
    public static PaginationInfoDTO of(final int currentPage, final int pageSize, final int totalItems, final int totalPages) {
        return new PaginationInfoDTO(currentPage, pageSize, totalItems, totalPages);
    }
}

package com.ktb.eatbookappbackend.domain.global.dto;

public record PaginationInfoDTO(
        int currentPage,
        int pageSize,
        int totalItems,
        int totalPages
) {}

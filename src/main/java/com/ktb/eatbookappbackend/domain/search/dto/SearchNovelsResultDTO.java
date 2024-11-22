package com.ktb.eatbookappbackend.domain.search.dto;

import com.ktb.eatbookappbackend.global.dto.PaginationInfoDTO;
import com.ktb.eatbookappbackend.domain.novel.dto.NovelDTO;
import java.util.List;

public record SearchNovelsResultDTO(
    PaginationInfoDTO pagination,
    List<NovelDTO> novels
) {

    public static SearchNovelsResultDTO of(PaginationInfoDTO pagination, List<NovelDTO> novels) {
        return new SearchNovelsResultDTO(pagination, novels);
    }
}

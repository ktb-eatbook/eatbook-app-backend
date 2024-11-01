package com.ktb.eatbookappbackend.domain.member.dto;

import com.ktb.eatbookappbackend.domain.global.dto.PaginationInfoDTO;
import com.ktb.eatbookappbackend.domain.novel.dto.NovelDTO;
import java.util.List;

public record BookmarkedNovelsPaginationDTO(
    PaginationInfoDTO pagination,
    List<NovelDTO> bookmarkedNovels
) {

    public static BookmarkedNovelsPaginationDTO of(PaginationInfoDTO pagination, List<NovelDTO> bookmarkedNovels) {
        return new BookmarkedNovelsPaginationDTO(pagination, bookmarkedNovels);
    }
}

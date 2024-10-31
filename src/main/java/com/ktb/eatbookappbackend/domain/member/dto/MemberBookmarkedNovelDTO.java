package com.ktb.eatbookappbackend.domain.member.dto;

import com.ktb.eatbookappbackend.entity.Novel;

public record MemberBookmarkedNovelDTO(
        String id,
        String title,
        String coverImageUrl,
        int viewCount,
        int favoriteCount,
        boolean isFavorited
) {
    public static MemberBookmarkedNovelDTO of(Novel novel, int favoriteCount, boolean isFavorited) {
        return new MemberBookmarkedNovelDTO(
                novel.getId(),
                novel.getTitle(),
                novel.getCoverImageUrl(),
                novel.getViewCount(),
                favoriteCount,
                isFavorited
        );
    }
}

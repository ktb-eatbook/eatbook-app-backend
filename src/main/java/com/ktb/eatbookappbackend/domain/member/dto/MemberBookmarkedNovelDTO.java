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
    public static MemberBookmarkedNovelDTO of(Novel novel) {
        return new MemberBookmarkedNovelDTO(
                novel.getId(),
                novel.getTitle(),
                novel.getCoverImageUrl(),
                novel.getViewCount(),
                0, // TO DO - 실제 좋아요 수 반환
                false // TO DO - 실제 좋아요 여부 반환
        );
    }
}

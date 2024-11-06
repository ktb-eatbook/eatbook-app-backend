package com.ktb.eatbookappbackend.domain.novel.dto;

import com.ktb.eatbookappbackend.entity.Novel;

import java.time.LocalDateTime;
import java.util.List;

public record NovelDTO(
        String id,
        String title,
        String coverImageUrl,
        List<String> authors,
        LocalDateTime createdAt,
        List<String> categories,
        boolean isCompleted,
        int viewCount,
        String summary,
        Integer publicationYear,
        int favoriteCount
) {

    public static NovelDTO of(final Novel novel, final int favoriteCount) {
        return new NovelDTO(
                novel.getId(),
                novel.getTitle(),
                novel.getCoverImageUrl(),
                novel.getNovelAuthors().stream()
                       .map(novelAuthor -> novelAuthor.getAuthor().getName())
                       .toList(),
                novel.getCreatedAt(),
                novel.getNovelCategories().stream()
                        .map(novelCategory -> novelCategory.getCategory().getName())
                        .toList(),
                novel.isCompleted(),
                novel.getViewCount(),
                novel.getSummary(),
                novel.getPublicationYear(),
                favoriteCount
        );
    }
}

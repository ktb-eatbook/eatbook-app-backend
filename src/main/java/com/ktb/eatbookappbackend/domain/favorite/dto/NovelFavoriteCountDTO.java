package com.ktb.eatbookappbackend.domain.favorite.dto;

public record NovelFavoriteCountDTO(
    String novelId,
    Long count
) {

    static public NovelFavoriteCountDTO of(String novelId, Long count) {
        return new NovelFavoriteCountDTO(novelId, count);
    }
}

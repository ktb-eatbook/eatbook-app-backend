package com.ktb.eatbookappbackend.domain.novel.dto;

public record NovelFileDTO(
    String presignedUrl
) {

    public static NovelFileDTO of(String presignedUrl) {
        return new NovelFileDTO(presignedUrl);
    }
}

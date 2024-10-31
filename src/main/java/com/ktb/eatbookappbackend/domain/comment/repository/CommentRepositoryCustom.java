package com.ktb.eatbookappbackend.domain.comment.repository;

public interface CommentRepositoryCustom {
    long countCommentsByNovelId(String novelId);
}

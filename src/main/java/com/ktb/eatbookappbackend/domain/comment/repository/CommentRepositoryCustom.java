package com.ktb.eatbookappbackend.domain.comment.repository;

public interface CommentRepositoryCustom {

    int countCommentsByEpisodeId(String episodeId);
}

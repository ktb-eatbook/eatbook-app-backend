package com.ktb.eatbookappbackend.domain.comment.repository;

import static com.ktb.eatbookappbackend.entity.QComment.comment;
import static com.ktb.eatbookappbackend.entity.QEpisode.episode;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

@Repository
public class CommentRepositoryCustomImpl implements CommentRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public CommentRepositoryCustomImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public int countCommentsByEpisodeId(String episodeId) {
        Long count = queryFactory
            .select(comment.count())
            .from(comment)
            .join(comment.episode, episode)
            .where(episode.id.eq(episodeId))
            .fetchOne();

        return count != null ? count.intValue() : 0;
    }
}

package com.ktb.eatbookappbackend.domain.comment.repository;

import static com.ktb.eatbookappbackend.entity.QComment.comment;
import static com.ktb.eatbookappbackend.entity.QEpisode.episode;
import static com.ktb.eatbookappbackend.entity.QMember.member;

import com.ktb.eatbookappbackend.domain.episode.dto.CommentDTO;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
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

    @Override
    public List<CommentDTO> findCommentDTOsByEpisodeId(String episodeId) {
        return queryFactory
            .select(Projections.constructor(CommentDTO.class,
                comment.id,
                comment.content,
                member.id,
                member.nickname,
                member.profileImageUrl,
                comment.createdAt
            ))
            .from(comment)
            .join(comment.member, member)
            .where(
                comment.episode.id.eq(episodeId),
                comment.deletedAt.isNull()
            )
            .orderBy(comment.createdAt.desc())
            .fetch();
    }
}

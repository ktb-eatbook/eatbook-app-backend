package com.ktb.eatbookappbackend.domain.favorite.repository;

import com.ktb.eatbookappbackend.domain.favorite.dto.NovelFavoriteCountDTO;
import com.ktb.eatbookappbackend.entity.QFavorite;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class FavoriteRepositoryCustomImpl implements FavoriteRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public FavoriteRepositoryCustomImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public List<NovelFavoriteCountDTO> findFavoriteCountsByNovelIds(List<String> novelIds) {
        QFavorite favorite = QFavorite.favorite;

        return queryFactory
            .select(Projections.constructor(NovelFavoriteCountDTO.class,
                favorite.novel.id,
                favorite.count()))
            .from(favorite)
            .where(favorite.novel.id.in(novelIds))
            .groupBy(favorite.novel.id)
            .fetch();
    }
}

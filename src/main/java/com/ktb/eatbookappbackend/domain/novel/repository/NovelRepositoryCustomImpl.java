package com.ktb.eatbookappbackend.domain.novel.repository;

import com.ktb.eatbookappbackend.entity.Novel;
import com.ktb.eatbookappbackend.entity.QAuthor;
import com.ktb.eatbookappbackend.entity.QCategory;
import com.ktb.eatbookappbackend.entity.QEpisode;
import com.ktb.eatbookappbackend.entity.QNovel;
import com.ktb.eatbookappbackend.entity.QNovelAuthor;
import com.ktb.eatbookappbackend.entity.QNovelCategory;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class NovelRepositoryCustomImpl implements NovelRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public NovelRepositoryCustomImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public List<Novel> findAllWithDetails() {
        QNovel novel = QNovel.novel;
        QNovelAuthor novelAuthor = QNovelAuthor.novelAuthor;
        QAuthor author = QAuthor.author;
        QNovelCategory novelCategory = QNovelCategory.novelCategory;
        QCategory category = QCategory.category;
        QEpisode episode = QEpisode.episode;

        return queryFactory
            .selectFrom(novel)
            .innerJoin(novel.novelAuthors, novelAuthor).fetchJoin()
            .innerJoin(novelAuthor.author, author).fetchJoin()
            .innerJoin(novel.novelCategories, novelCategory).fetchJoin()
            .innerJoin(novelCategory.category, category).fetchJoin()
            .leftJoin(novel.episodes, episode).fetchJoin()
            .fetch();
    }
}

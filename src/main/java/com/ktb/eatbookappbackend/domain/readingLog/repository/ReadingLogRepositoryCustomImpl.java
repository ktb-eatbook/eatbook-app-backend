package com.ktb.eatbookappbackend.domain.readingLog.repository;

import com.ktb.eatbookappbackend.entity.QReadingLog;
import com.ktb.eatbookappbackend.entity.ReadingLog;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Optional;

public class ReadingLogRepositoryCustomImpl implements ReadingLogRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public ReadingLogRepositoryCustomImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public Optional<ReadingLog> findLastReadEpisode(String memberId, String novelId) {
        QReadingLog readingLog = QReadingLog.readingLog;

        ReadingLog result = queryFactory
            .selectFrom(readingLog)
            .where(
                readingLog.member.id.eq(memberId),
                readingLog.novel.id.eq(novelId)
            )
            .orderBy(readingLog.updatedAt.desc())
            .limit(1)
            .fetchOne();

        return Optional.ofNullable(result);
    }
}

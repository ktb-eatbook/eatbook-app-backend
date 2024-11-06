package com.ktb.eatbookappbackend.domain.readingLog.repository;

import com.ktb.eatbookappbackend.entity.ReadingLog;
import java.util.Optional;

public interface ReadingLogRepositoryCustom {

    Optional<ReadingLog> findLastReadEpisode(String memberId, String novelId);
}

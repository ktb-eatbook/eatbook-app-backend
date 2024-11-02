package com.ktb.eatbookappbackend.domain.readingLog.repository;

import com.ktb.eatbookappbackend.entity.ReadingLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReadingLogRepository extends JpaRepository<ReadingLog, String>, ReadingLogRepositoryCustom {

}

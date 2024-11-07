package com.ktb.eatbookappbackend.domain.search.repository;

import com.ktb.eatbookappbackend.entity.SearchLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SearchLogRepository extends JpaRepository<SearchLog, Long> {

}

package com.ktb.eatbookappbackend.domain.novel.repository;

import com.ktb.eatbookappbackend.entity.Episode;
import com.ktb.eatbookappbackend.entity.Novel;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface NovelRepository extends JpaRepository<Novel, Long> {

    Optional<Novel> findById(String id);

    @Query(value = "SELECT e FROM Episode e WHERE e.novel.id = :novelId and e.deletedAt IS NULL")
    List<Episode> findEpisodesByNovelId(@Param("novelId") String novelId, Sort sort);
}

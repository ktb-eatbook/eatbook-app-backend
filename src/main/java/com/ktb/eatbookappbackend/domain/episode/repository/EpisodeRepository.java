package com.ktb.eatbookappbackend.domain.episode.repository;

import com.ktb.eatbookappbackend.entity.Episode;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EpisodeRepository extends JpaRepository<Episode, String> {

    @Query("SELECT e FROM Episode e WHERE e.novel.id = :novelId AND e.releasedDate IS NOT NULL ORDER BY e.releasedDate DESC")
    List<Episode> findLatestEpisodesByNovel(@Param("novelId") String novelId);
}

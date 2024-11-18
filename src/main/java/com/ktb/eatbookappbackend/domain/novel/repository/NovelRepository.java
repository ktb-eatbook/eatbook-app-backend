package com.ktb.eatbookappbackend.domain.novel.repository;

import com.ktb.eatbookappbackend.entity.Episode;
import com.ktb.eatbookappbackend.entity.Novel;
import com.ktb.eatbookappbackend.entity.constant.EpisodeReleaseStatus;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NovelRepository extends JpaRepository<Novel, String>, NovelRepositoryCustom {

    Optional<Novel> findById(String id);

    default List<Episode> findPublicEpisodesByNovelId(String novelId, Sort sort) {
        return findEpisodesByNovelId(novelId, sort, EpisodeReleaseStatus.PUBLIC);
    }

    @Query(value = "SELECT e FROM Episode e WHERE e.novel.id = :novelId and e.deletedAt IS NULL and e.releaseStatus = :releaseStatus")
    List<Episode> findEpisodesByNovelId(@Param("novelId") String novelId, Sort sort,
        @Param("releaseStatus") EpisodeReleaseStatus releaseStatus);
}

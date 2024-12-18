package com.ktb.eatbookappbackend.domain.episode.repository;

import com.ktb.eatbookappbackend.entity.Episode;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EpisodeRepository extends JpaRepository<Episode, String> {

    Optional<Episode> findById(String id);
}

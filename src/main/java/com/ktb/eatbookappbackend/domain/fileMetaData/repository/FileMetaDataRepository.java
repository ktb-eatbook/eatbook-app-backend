package com.ktb.eatbookappbackend.domain.fileMetaData.repository;

import com.ktb.eatbookappbackend.entity.FileMetadata;
import com.ktb.eatbookappbackend.entity.constant.FileType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FileMetaDataRepository extends JpaRepository<FileMetadata, String> {

    @Query("SELECT CASE WHEN COUNT(f) > 0 THEN true ELSE false END " +
        "FROM FileMetadata f WHERE f.episode.id = :episodeId AND f.type = :type")
    boolean existsByEpisodeIdAndType(@Param("episodeId") String episodeId, @Param("type") FileType type);

    @Query("SELECT f FROM FileMetadata f WHERE f.episode.id = :episodeId AND f.type = :type")
    Optional<FileMetadata> findFileIdByEpisodeIdAndType(@Param("episodeId") String episodeId, @Param("type") FileType type);
}

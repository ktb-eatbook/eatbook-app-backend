package com.ktb.eatbookappbackend.domain.comment.repository;

import com.ktb.eatbookappbackend.entity.Comment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentRepository extends JpaRepository<Comment, String>, CommentRepositoryCustom {

    @Query("SELECT c FROM Comment c WHERE c.episode.id = :episodeId AND c.deletedAt IS NULL ORDER BY c.createdAt DESC")
    List<Comment> findCommentsByEpisodeId(@Param("episodeId") String episodeId);
}
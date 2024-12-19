package com.ktb.eatbookappbackend.domain.comment.repository;

import com.ktb.eatbookappbackend.domain.episode.dto.CommentDTO;
import com.ktb.eatbookappbackend.entity.Comment;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface CommentRepository extends JpaRepository<Comment, String>, CommentRepositoryCustom {

    List<CommentDTO> findCommentDTOsByEpisodeId(@Param("episodeId") String episodeId);

    @Query("SELECT c FROM Comment c JOIN FETCH c.member WHERE c.id = :commentId AND c.deletedAt IS NULL")
    Optional<Comment> findByIdAndDeletedAtIsNull(@Param("commentId") String commentId);

    @Transactional
    @Modifying
    @Query("UPDATE Comment c SET c.reportCount = c.reportCount + 1 WHERE c.id = :commentId")
    void incrementReportCount(@Param("commentId") String commentId);
}
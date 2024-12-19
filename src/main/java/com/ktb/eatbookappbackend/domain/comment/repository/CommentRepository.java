package com.ktb.eatbookappbackend.domain.comment.repository;

import com.ktb.eatbookappbackend.domain.episode.dto.CommentDetailDTO;
import com.ktb.eatbookappbackend.entity.Comment;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentRepository extends JpaRepository<Comment, String>, CommentRepositoryCustom {

    List<CommentDetailDTO> findCommentDetailDTOsByEpisodeId(@Param("episodeId") String episodeId);

    @Query("SELECT c FROM Comment c JOIN FETCH c.member WHERE c.id = :commentId AND c.deletedAt IS NULL")
    Optional<Comment> findByIdAndDeletedAtIsNull(@Param("commentId") String commentId);
}
package com.ktb.eatbookappbackend.domain.comment.repository;

import com.ktb.eatbookappbackend.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, String>, CommentRepositoryCustom {
}
package com.ktb.eatbookappbackend.domain.novel.repository;

import com.ktb.eatbookappbackend.entity.Novel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NovelRepository extends JpaRepository<Novel, Long> {
    Optional<Novel> findById(String id);
}

package com.ktb.eatbookappbackend.domain.bookmark.repository;

import com.ktb.eatbookappbackend.entity.Bookmark;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    @Query("SELECT b FROM Bookmark b JOIN FETCH b.novel WHERE b.member.id = :memberId ORDER BY b.createdAt DESC")
    Page<Bookmark> findByMemberIdWithNovel(String memberId, Pageable pageable);
}

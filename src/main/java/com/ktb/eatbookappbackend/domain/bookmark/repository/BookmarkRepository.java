package com.ktb.eatbookappbackend.domain.bookmark.repository;

import com.ktb.eatbookappbackend.entity.Bookmark;
import com.ktb.eatbookappbackend.entity.Member;
import com.ktb.eatbookappbackend.entity.Novel;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    @Query("SELECT b FROM Bookmark b JOIN FETCH b.novel WHERE b.member.id = :memberId ORDER BY b.createdAt DESC")
    Page<Bookmark> findByMemberIdWithNovel(@Param("memberId") String memberId, Pageable pageable);

    boolean existsByNovelAndMember(Novel novel, Member member);

    Optional<Bookmark> findByNovelAndMember(Novel novel, Member member);
}

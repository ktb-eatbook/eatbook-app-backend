package com.ktb.eatbookappbackend.domain.favorite.repository;

import com.ktb.eatbookappbackend.entity.Favorite;
import com.ktb.eatbookappbackend.entity.IdClass.FavoriteId;
import com.ktb.eatbookappbackend.entity.Member;
import com.ktb.eatbookappbackend.entity.Novel;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FavoriteRepository extends JpaRepository<Favorite, FavoriteId>, FavoriteRepositoryCustom {

    boolean existsByNovelAndMember(Novel novel, Member member);

    int countByNovelId(String novelId);

    Optional<Favorite> findByNovelAndMember(Novel novel, Member member);

    @Query("SELECT f.novel.id FROM Favorite f WHERE f.member.id = :memberId")
    List<String> findNovelIdsByMemberId(@Param("memberId") String memberId);
}

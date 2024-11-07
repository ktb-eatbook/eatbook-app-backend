package com.ktb.eatbookappbackend.domain.favorite.repository;

import com.ktb.eatbookappbackend.entity.Favorite;
import com.ktb.eatbookappbackend.entity.Member;
import com.ktb.eatbookappbackend.entity.Novel;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteRepository extends JpaRepository<Favorite, String> {

    boolean existsByNovelAndMember(Novel novel, Member member);

    int countByNovelId(String novelId);

    Optional<Favorite> findByNovelAndMember(Novel novel, Member member);
}

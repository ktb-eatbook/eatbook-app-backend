package com.ktb.eatbookappbackend.favorite.fixture;

import com.ktb.eatbookappbackend.entity.Bookmark;
import com.ktb.eatbookappbackend.entity.Favorite;
import com.ktb.eatbookappbackend.entity.Member;
import com.ktb.eatbookappbackend.entity.Novel;
import java.util.UUID;
import org.springframework.test.util.ReflectionTestUtils;

public class FavoriteFixture {

    public static Favorite createFavorite(Member member, Novel novel) {
        return Favorite.builder()
            .member(member)
            .novel(novel)
            .build();
    }
}

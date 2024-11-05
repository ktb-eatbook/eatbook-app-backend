package com.ktb.eatbookappbackend.bookmark.fixture;

import com.ktb.eatbookappbackend.entity.Bookmark;
import com.ktb.eatbookappbackend.entity.Member;
import com.ktb.eatbookappbackend.entity.Novel;

public class BookmarkFixture {

    public static Bookmark createBookmark(Member member, Novel novel) {
        return Bookmark.builder()
            .member(member)
            .novel(novel)
            .build();
    }
}

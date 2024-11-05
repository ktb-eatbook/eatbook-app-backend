package com.ktb.eatbookappbackend.novelBookmark.fixture;

import com.ktb.eatbookappbackend.entity.Bookmark;
import com.ktb.eatbookappbackend.entity.Member;
import com.ktb.eatbookappbackend.entity.Novel;
import java.util.UUID;
import org.springframework.test.util.ReflectionTestUtils;

public class BookmarkFixture {

    public static Bookmark createBookmark(Member member, Novel novel) {
        Bookmark bookmark = Bookmark.builder()
            .member(member)
            .novel(novel)
            .build();
        String bookmarkId = UUID.randomUUID().toString();
        ReflectionTestUtils.setField(novel, "id", bookmarkId);
        return bookmark;
    }
}

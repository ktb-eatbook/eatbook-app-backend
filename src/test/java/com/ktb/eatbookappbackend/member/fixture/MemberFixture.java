package com.ktb.eatbookappbackend.member.fixture;

import com.ktb.eatbookappbackend.entity.Bookmark;
import com.ktb.eatbookappbackend.entity.Member;
import com.ktb.eatbookappbackend.entity.Novel;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.UUID;

public class MemberFixture {

    public static final int PAGE = 1;
    public static final int SIZE = 10;
    public static final int TOTAL_ITEMS = 1;
    public static final int TOTAL_PAGES = 1;
    public static final int FAVORITE_COUNT = 5;
    public static final boolean IS_FAVORITED = true;

    public static final int EMPTY_TOTAL_ITEMS = 0;
    public static final int EMPTY_TOTAL_PAGES = 1;

    public static Member createMember() {
        Member member = Member.builder()
                .nickname("nickname")
                .profileImageUrl("profile")
                .email("email")
                .build();
        String memberId = UUID.randomUUID().toString();
        ReflectionTestUtils.setField(member, "id", memberId);
        return member;
    }

    public static Novel createNovel() {
        Novel novel = Novel.builder()
                .title("Novel Title")
                .coverImageUrl("coverImageUrl")
                .summary("Novel Summary")
                .isCompleted(true)
                .build();
        String novelID = UUID.randomUUID().toString();
        ReflectionTestUtils.setField(novel, "id", novelID);
        return novel;
    }

    public static Bookmark createBookmark(Novel novel, Member member) {
        return Bookmark.builder()
                .novelId(novel.getId())
                .memberId(member.getId())
                .novel(novel)
                .member(member)
                .build();
    }
}

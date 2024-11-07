package com.ktb.eatbookappbackend.member.fixture;

import com.ktb.eatbookappbackend.entity.Member;
import java.util.UUID;
import org.springframework.test.util.ReflectionTestUtils;

public class MemberFixture {

    public static final int PAGE = 1;
    public static final int SIZE = 10;
    public static final int TOTAL_ITEMS = 1;
    public static final int TOTAL_PAGES = 1;
    public static final int FAVORITE_COUNT = 5;

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
}

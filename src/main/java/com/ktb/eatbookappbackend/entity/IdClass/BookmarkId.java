package com.ktb.eatbookappbackend.entity.IdClass;

import com.ktb.eatbookappbackend.entity.Member;
import com.ktb.eatbookappbackend.entity.Novel;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookmarkId {

    private Novel novel;
    private Member member;

    @Builder
    public BookmarkId(Novel novel, Member member) {
        this.novel = novel;
        this.member = member;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookmarkId that = (BookmarkId) o;
        return Objects.equals(novel, that.novel) && Objects.equals(member, that.member);
    }

    @Override
    public int hashCode() {
        return Objects.hash(novel, member);
    }
}

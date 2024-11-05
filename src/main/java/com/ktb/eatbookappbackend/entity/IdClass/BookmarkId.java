package com.ktb.eatbookappbackend.entity.IdClass;

import java.util.Objects;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookmarkId {

    private String novel;
    private String member;

    @Builder
    public BookmarkId(String novelId, String memberId) {
        this.novel = novelId;
        this.member = memberId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BookmarkId that = (BookmarkId) o;
        return Objects.equals(novel, that.novel) && Objects.equals(member, that.member);
    }

    @Override
    public int hashCode() {
        return Objects.hash(novel, member);
    }
}

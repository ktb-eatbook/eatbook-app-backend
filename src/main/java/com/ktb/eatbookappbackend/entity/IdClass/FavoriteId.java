package com.ktb.eatbookappbackend.entity.IdClass;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FavoriteId {

    private String novelId;
    private String memberId;

    @Builder
    public FavoriteId(String novelId, String memberId) {
        this.novelId = novelId;
        this.memberId = memberId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FavoriteId that = (FavoriteId) o;
        return Objects.equals(novelId, that.novelId) && Objects.equals(memberId, that.memberId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(novelId, memberId);
    }
}
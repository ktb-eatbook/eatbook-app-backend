package com.ktb.eatbookappbackend.entity.IdClass;

import com.ktb.eatbookappbackend.entity.Category;
import com.ktb.eatbookappbackend.entity.Novel;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NovelCategoryId {
    private Novel novel;
    private Category category;

    @Builder
    public NovelCategoryId(Novel novel, Category category) {
        this.novel = novel;
        this.category = category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NovelCategoryId that = (NovelCategoryId) o;
        return Objects.equals(novel, that.novel) && Objects.equals(category, that.category);
    }

    @Override
    public int hashCode() {
        return Objects.hash(novel, category);
    }
}

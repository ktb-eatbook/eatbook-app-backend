package com.ktb.eatbookappbackend.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NovelAuthorId implements Serializable {

    private Novel novel;
    private Author author;

    @Builder
    public NovelAuthorId(Novel novel, Author author) {
        this.novel = novel;
        this.author = author;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NovelAuthorId that = (NovelAuthorId) o;
        return Objects.equals(novel, that.novel) && Objects.equals(author, that.author);
    }

    @Override
    public int hashCode() {
        return Objects.hash(novel, author);
    }
}

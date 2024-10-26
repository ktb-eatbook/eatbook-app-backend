package com.ktb.eatbookappbackend.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Getter
@NoArgsConstructor
@Entity
@IdClass(NovelAuthorId.class)
public class NovelAuthor {

    @Id
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "novel_id", nullable = false)
    private Novel novel;

    @Id
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "author_id", nullable = false)
    private Author author;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Builder
    public NovelAuthor(Novel novel, Author author) {
        this.novel = novel;
        this.author = author;
    }

    public void setNovel(Novel novel) {
        this.novel = novel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NovelAuthor that = (NovelAuthor) o;
        return Objects.equals(novel, that.novel) && Objects.equals(author, that.author);
    }

    @Override
    public int hashCode() {
        return Objects.hash(novel, author);
    }
}

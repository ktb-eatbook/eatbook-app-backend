package com.ktb.eatbookappbackend.entity;


import com.ktb.eatbookappbackend.entity.base.SoftDeletableEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Novel extends SoftDeletableEntity {

    @Id
    @Column(length = 36)
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @NotNull
    @Column(nullable = false)
    private String title;

    private String coverImageUrl;

    @Column(length = 1000)
    private String summary;

    @NotNull
    @Column(nullable = false)
    private int viewCount = 0;

    @NotNull
    @Column(nullable = false)
    private boolean isCompleted;

    private Integer publicationYear;

    @OneToMany(mappedBy = "novel", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<NovelCategory> novelCategories = new ArrayList<>();

    @OneToMany(mappedBy = "novel", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<NovelAuthor> novelAuthors = new ArrayList<>();

    @Builder
    public Novel(String title, String coverImageUrl, String summary, boolean isCompleted) {
        this.title = title;
        this.coverImageUrl = coverImageUrl;
        this.summary = summary;
        this.isCompleted = isCompleted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Novel novel = (Novel) o;
        return Objects.equals(id, novel.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}

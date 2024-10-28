package com.ktb.eatbookappbackend.entity;

import com.ktb.eatbookappbackend.entity.base.SoftDeletableEntity;
import com.ktb.eatbookappbackend.entity.constant.EpisodeReleaseStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Episode extends SoftDeletableEntity {

    @Id
    @Column(length = 36)
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @NotNull
    @Column(nullable = false)
    private String title;

    @NotNull
    @Column(nullable = false)
    private int chapterNumber;

    @Column(length = 1000)
    private LocalDateTime uploadDate;

    @NotNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EpisodeReleaseStatus releaseStatus = EpisodeReleaseStatus.PUBLIC;

    @NotNull
    @Column(nullable = false)
    private int viewCount = 0;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "novel_id", nullable = false)
    private Novel novel;

    @Builder
    public Episode(String title, int chapterNumber, LocalDateTime uploadDate, EpisodeReleaseStatus releaseStatus, Novel novel) {
        this.title = title;
        this.chapterNumber = chapterNumber;
        this.uploadDate = uploadDate;
        this.releaseStatus = releaseStatus != null ? releaseStatus : EpisodeReleaseStatus.PUBLIC;
        this.novel = novel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Episode episode = (Episode) o;
        return Objects.equals(id, episode.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}

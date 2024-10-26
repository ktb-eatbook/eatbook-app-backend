package com.ktb.eatbookappbackend.entity;

import com.ktb.eatbookappbackend.entity.base.SoftDeletableEntity;
import com.ktb.eatbookappbackend.entity.constant.EpisodeReleaseStatus;
import com.ktb.eatbookappbackend.entity.constant.Role;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Episode extends SoftDeletableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "VARCHAR(36)")
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private int chapterNumber;

    @Column(length = 1000)
    private LocalDateTime uploadDate;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EpisodeReleaseStatus releaseStatus = EpisodeReleaseStatus.PUBLIC;

    @Column(nullable = false)
    private int viewCount = 0;

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

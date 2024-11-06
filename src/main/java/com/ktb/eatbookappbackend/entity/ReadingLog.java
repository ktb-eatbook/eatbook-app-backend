package com.ktb.eatbookappbackend.entity;

import com.ktb.eatbookappbackend.entity.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import java.sql.Time;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"member_id", "novel_id", "episode_id"})})
public class ReadingLog extends BaseEntity {

    @Id
    @Column(length = 36)
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "page_number", nullable = false)
    private int pageNumber;

    @Column(name = "tts_last_position_seconds", nullable = false)
    private Time ttsLastPositionSeconds;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    @NotNull
    private Member member;

    @ManyToOne
    @JoinColumn(name = "novel_id", nullable = false)
    @NotNull
    private Novel novel;

    @ManyToOne
    @JoinColumn(name = "episode_id", nullable = false)
    @NotNull
    private Episode episode;

    @Builder
    public ReadingLog(int pageNumber, Time ttsLastPositionSeconds, Member member, Novel novel, Episode episode) {
        this.pageNumber = pageNumber;
        this.ttsLastPositionSeconds = ttsLastPositionSeconds;
        this.member = member;
        this.novel = novel;
        this.episode = episode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ReadingLog that = (ReadingLog) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}

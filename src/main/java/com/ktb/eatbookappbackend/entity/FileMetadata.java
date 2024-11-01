package com.ktb.eatbookappbackend.entity;

import com.ktb.eatbookappbackend.entity.base.BaseEntity;
import com.ktb.eatbookappbackend.entity.constant.FileType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class FileMetadata extends BaseEntity {

    @Id
    @Column(length = 36)
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @NotNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private FileType type;

    @NotNull
    @Column(nullable = false)
    private String path;

    @NotNull
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "episode_id", nullable = false)
    private Episode episode;

    @Builder
    public FileMetadata(FileType type, String path, Episode episode) {
        this.type = type;
        this.path = path;
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
        FileMetadata that = (FileMetadata) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}

package com.ktb.eatbookappbackend.entity;

import com.ktb.eatbookappbackend.entity.IdClass.BookmarkId;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@IdClass(BookmarkId.class)
public class Bookmark {

    @Id
    @ManyToOne
    @JoinColumn(name = "novel_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Novel novel;

    @Id
    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    @Column(nullable = false)
    @NotNull
    @CreatedDate
    private LocalDateTime createdAt;

    @Builder
    public Bookmark(Novel novel, Member member) {
        this.novel = novel;
        this.member = member;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Bookmark bookmark = (Bookmark) o;
        return Objects.equals(novel, bookmark.novel) && Objects.equals(member, bookmark.member);
    }

    @Override
    public int hashCode() {
        return Objects.hash(novel, member);
    }
}

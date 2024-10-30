package com.ktb.eatbookappbackend.entity;

import com.ktb.eatbookappbackend.entity.IdClass.BookmarkId;
import com.ktb.eatbookappbackend.entity.IdClass.FavoriteId;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@IdClass(FavoriteId.class)
public class Favorite {

    @Id
    @Column(name = "novel_id", length = 36)
    private String novelId;

    @Id
    @Column(name = "member_id", length = 36)
    private String memberId;

    @Column(nullable = false)
    @NotNull
    @CreatedDate
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    @NotNull
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Novel novel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    @NotNull
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    @Builder
    public Favorite(String novelId, String memberId, Novel novel, Member member) {
        this.novelId = novelId;
        this.memberId = memberId;
        this.novel = novel;
        this.member = member;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Favorite favorite = (Favorite) o;
        return Objects.equals(novelId, favorite.novelId) && Objects.equals(memberId, favorite.memberId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(novelId, memberId);
    }
}

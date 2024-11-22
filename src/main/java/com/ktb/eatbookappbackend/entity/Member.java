package com.ktb.eatbookappbackend.entity;

import com.ktb.eatbookappbackend.entity.base.SoftDeletableEntity;
import com.ktb.eatbookappbackend.entity.constant.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Member extends SoftDeletableEntity {

    @Id
    @Column(length = 36)
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @NotNull
    @Column(nullable = false, length = 100)
    private String nickname;

    @NotNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role = Role.MEMBER;

    private String profileImageUrl;

    @Column
    private LocalDateTime lastLogin;

    private String passwordHash;

    @NotNull
    @Column(nullable = false)
    private String email;

    @Builder
    public Member(String nickname, String profileImageUrl, String email) {
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Member member = (Member) o;
        return Objects.equals(id, member.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}

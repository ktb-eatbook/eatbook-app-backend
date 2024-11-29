package com.ktb.eatbookappbackend.entity;

import com.ktb.eatbookappbackend.entity.base.SoftDeletableEntity;
import com.ktb.eatbookappbackend.entity.constant.AgeGroup;
import com.ktb.eatbookappbackend.entity.constant.AgeGroupConverter;
import com.ktb.eatbookappbackend.entity.constant.Gender;
import com.ktb.eatbookappbackend.entity.constant.Role;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
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
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @NotNull
    @Column(nullable = false, columnDefinition = "TINYINT UNSIGNED")
    @Convert(converter = AgeGroupConverter.class)
    private AgeGroup ageGroup;

    @NotNull
    @Column(nullable = false)
    private String email;

    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL)
    private MemberSetting memberSetting;

    @Builder
    public Member(final String nickname, final String profileImageUrl, final String email, final Gender gender, final AgeGroup ageGroup) {
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
        this.email = email;
        this.gender = gender;
        this.ageGroup = ageGroup;
    }

    public void updateMemberSetting(final MemberSetting memberSetting) {
        this.memberSetting = memberSetting;
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

package com.ktb.eatbookappbackend.entity;

import com.ktb.eatbookappbackend.entity.base.BaseEntity;
import com.ktb.eatbookappbackend.entity.constant.FontPreference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Entity
public class MemberSetting extends BaseEntity {

    @Id
    @Column(length = 36)
    private String id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id", referencedColumnName = "id", nullable = false)
    private Member member;

    @NotNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private FontPreference fontPreference = FontPreference.SUIT;

    @NotNull
    @Column(nullable = false)
    private String themePreference = "221a14";

    @NotNull
    @Column(nullable = false)
    private Double ttsSpeed = 1.0;

    @NotNull
    @Column(nullable = false)
    private int fontSize = 15;

    @Builder()
    public MemberSetting(final Member member) {
        this.member = member;
        member.updateMemberSetting(this);
    }
}

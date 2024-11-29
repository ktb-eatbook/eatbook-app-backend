package com.ktb.eatbookappbackend.oauth.dto;

import com.ktb.eatbookappbackend.entity.Member;
import java.util.List;

public record MemberDTO(
    String id,
    String nickname,
    String profileImage,
    String email,
    List<String> bookmarkedNovelIds,
    List<String> favoritedNovelIds,
    MemberSettingDTO memberSetting
) {

    public static MemberDTO of(
        Member member,
        List<String> bookmarkedNovelIds,
        List<String> favoritedNovelIds,
        MemberSettingDTO memberSetting
    ) {
        return new MemberDTO(
            member.getId(),
            member.getNickname(),
            member.getProfileImageUrl(),
            member.getEmail(),
            bookmarkedNovelIds,
            favoritedNovelIds,
            memberSetting
        );
    }
}

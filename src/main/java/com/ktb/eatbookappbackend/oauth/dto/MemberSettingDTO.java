package com.ktb.eatbookappbackend.oauth.dto;

import com.ktb.eatbookappbackend.entity.MemberSetting;

public record MemberSettingDTO(
    String fontPreference,
    String themePreference,
    Double ttsSpeed,
    Integer fontSize
) {

    public static MemberSettingDTO of(MemberSetting memberSetting) {
        return new MemberSettingDTO(
            memberSetting.getFontPreference().toString(),
            memberSetting.getThemePreference(),
            memberSetting.getTtsSpeed(),
            memberSetting.getFontSize());
    }
}
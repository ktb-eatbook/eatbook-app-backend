package com.ktb.eatbookappbackend.oauth.service;

import com.ktb.eatbookappbackend.domain.bookmark.repository.BookmarkRepository;
import com.ktb.eatbookappbackend.domain.favorite.repository.FavoriteRepository;
import com.ktb.eatbookappbackend.domain.member.repository.MemberRepository;
import com.ktb.eatbookappbackend.domain.memberSetting.repository.MemberSettingRepository;
import com.ktb.eatbookappbackend.entity.Member;
import com.ktb.eatbookappbackend.entity.MemberSetting;
import com.ktb.eatbookappbackend.entity.constant.AgeGroup;
import com.ktb.eatbookappbackend.entity.constant.Gender;
import com.ktb.eatbookappbackend.oauth.dto.MemberDTO;
import com.ktb.eatbookappbackend.oauth.dto.MemberSettingDTO;
import com.ktb.eatbookappbackend.oauth.dto.SignupResponseDTO;
import com.ktb.eatbookappbackend.oauth.exception.SignupException;
import com.ktb.eatbookappbackend.oauth.message.AuthErrorCode;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final MemberRepository memberRepository;
    private final MemberSettingRepository memberSettingRepository;
    private final BookmarkRepository bookmarkRepository;
    private final FavoriteRepository favoriteRepository;

    public SignupResponseDTO signUp(String email, String nickname, String profileImageUrl, Gender gender, AgeGroup ageGroup) {
        Member newMember = createMember(email, nickname, profileImageUrl, gender, ageGroup);
        MemberSettingDTO memberSettingDTO = MemberSettingDTO.of(newMember.getMemberSetting());
        MemberDTO memberDTO = MemberDTO.of(newMember, List.of(), List.of(), memberSettingDTO);
        return SignupResponseDTO.of(memberDTO);
    }

    public MemberDTO login(Member member) {
        MemberSettingDTO memberSetting = MemberSettingDTO.of(member.getMemberSetting());
        List<String> bookmarkedNovelIds = bookmarkRepository.findNovelIdsByMemberId(member.getId());
        List<String> favoritedNovelIds = favoriteRepository.findNovelIdsByMemberId(member.getId());
        return MemberDTO.of(member, bookmarkedNovelIds, favoritedNovelIds, memberSetting);
    }

    @Transactional
    public Member createMember(String email, String nickname, String profileImageUrl, Gender gender, AgeGroup ageGroup) {
        if (memberRepository.existsByEmailAndDeletedAtIsNull(email)) {
            throw new SignupException(AuthErrorCode.EMAIL_DUPLICATED);
        }

        Member member = Member.builder()
            .email(email)
            .nickname(nickname)
            .profileImageUrl(profileImageUrl)
            .gender(gender)
            .ageGroup(ageGroup)
            .build();

        memberRepository.save(member);

        MemberSetting memberSetting = MemberSetting.builder()
            .member(member)
            .build();

        memberSettingRepository.save(memberSetting);
        member.updateMemberSetting(memberSetting);

        return member;
    }
}

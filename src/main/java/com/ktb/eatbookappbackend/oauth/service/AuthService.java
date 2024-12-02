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

    /**
     * 사용자 가입 프로세스를 처리합니다.
     *
     * @param email 사용자의 이메일. 반드시 고유하고 null이 아니어야 합니다.
     * @param nickname 사용자의 닉네임. null이 아니어야 합니다.
     * @param profileImageUrl 사용자의 프로필 이미지 URL. null일 수 있습니다.
     * @param gender 사용자의 성별. null이 아니어야 합니다.
     * @param ageGroup 사용자의 연령대. null이 아니어야 합니다.
     * @return {@link SignupResponseDTO}로, 가입한 사용자의 정보를 포함합니다.
     * @throws SignupException 이메일이 이미 등록된 경우 발생합니다.
     */
    public SignupResponseDTO signUp(String email, String nickname, String profileImageUrl, Gender gender, AgeGroup ageGroup) {
        Member newMember = createMember(email, nickname, profileImageUrl, gender, ageGroup);
        MemberSettingDTO memberSettingDTO = MemberSettingDTO.of(newMember.getMemberSetting());
        MemberDTO memberDTO = MemberDTO.of(newMember, List.of(), List.of(), memberSettingDTO);
        return SignupResponseDTO.of(memberDTO);
    }

    /**
     * 로그인 프로세스를 처리합니다.
     *
     * @param member 로그인을 시도하는 사용자. null이 아니어야 합니다.
     * @return {@link MemberDTO}로, 로그인한 사용자의 정보를 포함합니다.
     *         사용자의 북마크한 소설 ID, 좋아요한 소설 ID,
     *         사용자 설정 정보를 포함합니다.
     */
    public MemberDTO login(Member member) {
        MemberSettingDTO memberSetting = MemberSettingDTO.of(member.getMemberSetting());
        List<String> bookmarkedNovelIds = bookmarkRepository.findNovelIdsByMemberId(member.getId());
        List<String> favoritedNovelIds = favoriteRepository.findNovelIdsByMemberId(member.getId());
        return MemberDTO.of(member, bookmarkedNovelIds, favoritedNovelIds, memberSetting);
    }

    /**
     * 시스템에 새로운 멤버를 생성합니다.
     *
     * @param email 멤버의 고유 이메일. null이 아니어야 합니다.
     * @param nickname 멤버의 닉네임. null이 아니어야 합니다.
     * @param profileImageUrl 멤버의 프로필 이미지 URL. null일 수 있습니다.
     * @param gender 멤버의 성별. null이 아니어야 합니다.
     * @param ageGroup 멤버의 연령대. null이 아니어야 합니다.
     * @return 새로 생성된 멤버.
     * @throws SignupException 이메일이 이미 시스템에 등록된 경우 발생합니다.
     */
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

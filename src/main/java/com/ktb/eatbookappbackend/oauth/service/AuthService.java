package com.ktb.eatbookappbackend.oauth.service;

import com.ktb.eatbookappbackend.domain.bookmark.repository.BookmarkRepository;
import com.ktb.eatbookappbackend.domain.favorite.repository.FavoriteRepository;
import com.ktb.eatbookappbackend.domain.member.repository.MemberRepository;
import com.ktb.eatbookappbackend.domain.memberSetting.repository.MemberSettingRepository;
import com.ktb.eatbookappbackend.entity.Member;
import com.ktb.eatbookappbackend.entity.MemberSetting;
import com.ktb.eatbookappbackend.oauth.dto.MemberDTO;
import com.ktb.eatbookappbackend.oauth.dto.MemberSettingDTO;
import com.ktb.eatbookappbackend.oauth.dto.SignupMemberDTO;
import com.ktb.eatbookappbackend.oauth.dto.SignupRequestDTO;
import com.ktb.eatbookappbackend.oauth.dto.SignupResponseDTO;
import com.ktb.eatbookappbackend.oauth.exception.SignupException;
import com.ktb.eatbookappbackend.oauth.jwt.JwtUtil;
import com.ktb.eatbookappbackend.oauth.message.AuthErrorCode;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final JwtUtil jwtUtil;
    private final MemberRepository memberRepository;
    private final MemberSettingRepository memberSettingRepository;
    private final BookmarkRepository bookmarkRepository;
    private final FavoriteRepository favoriteRepository;

    /**
     * 로그인 프로세스를 처리합니다.
     *
     * @param member 로그인을 시도하는 사용자. null이 아니어야 합니다.
     * @return {@link MemberDTO}로, 로그인한 사용자의 정보를 포함합니다. 사용자의 북마크한 소설 ID, 좋아요한 소설 ID, 사용자 설정 정보를 포함합니다.
     */
    public MemberDTO login(Member member) {
        MemberSettingDTO memberSetting = MemberSettingDTO.of(member.getMemberSetting());
        List<String> bookmarkedNovelIds = bookmarkRepository.findNovelIdsByMemberId(member.getId());
        List<String> favoritedNovelIds = favoriteRepository.findNovelIdsByMemberId(member.getId());
        return MemberDTO.of(member, bookmarkedNovelIds, favoritedNovelIds, memberSetting);
    }

    /**
     * 가입 프로세스를 처리합니다.
     *
     * @param signupRequestDTO 가입 요청 데이터 전송 객체. 가입에 필요한 정보를 담고 있습니다.
     * @return {@link SignupResponseDTO} 가입 응답 데이터 전송 객체. 멤버 정보, 북마크한 소설 ID, 좋아요한 소설 ID, 멤버 설정 정보를 포함합니다.
     * @throws SignupException 시스템에 이미 존재하는 이메일이 가입 요청에 포함된 경우 발생합니다.
     */
    public SignupResponseDTO signUp(SignupRequestDTO signupRequestDTO) {
        SignupMemberDTO signupMemberDTO = validateAndExtractSignupInfo(signupRequestDTO);
        Member newMember = createMember(signupMemberDTO);
        MemberSettingDTO memberSettingDTO = MemberSettingDTO.of(newMember.getMemberSetting());
        MemberDTO memberDTO = MemberDTO.of(newMember, List.of(), List.of(), memberSettingDTO);
        return SignupResponseDTO.of(memberDTO);
    }

    /**
     * 제공된 요청 DTO에서 가입 정보를 유효성 검사하고 추출합니다.
     *
     * @param signupRequestDTO 가입 요청 데이터 전송 객체. 필요한 정보를 담고 있습니다.
     * @return {@link SignupMemberDTO} 객체. 추출된 가입 정보를 포함합니다.
     * @throws SignupException 가입 토큰이 유효하지 않은 경우 발생합니다.
     */
    private SignupMemberDTO validateAndExtractSignupInfo(SignupRequestDTO signupRequestDTO) {
        String signupToken = signupRequestDTO.token();
        if (!jwtUtil.validateSignupToken(signupToken)) {
            throw new SignupException(AuthErrorCode.SIGNUP_TOKEN_INVALID);
        }

        Map<String, String> claims = jwtUtil.extractSignupClaims(signupToken);
        return SignupMemberDTO.of(claims, signupRequestDTO.gender(), signupRequestDTO.getAgeGroupEnum());
    }

    /**
     * 새로운 멤버를 생성합니다.
     *
     * @param signupMemberDTO 멤버 생성에 필요한 정보를 담고 있는 DTO.
     * @return 새로 생성된 멤버.
     * @throws SignupException DTO에 제공된 이메일이 시스템에 이미 존재하는 경우.
     */
    @Transactional
    protected Member createMember(SignupMemberDTO signupMemberDTO) {
        String email = signupMemberDTO.email();
        if (memberRepository.existsByEmailAndDeletedAtIsNull(email)) {
            throw new SignupException(AuthErrorCode.EMAIL_DUPLICATED);
        }

        Member member = Member.builder()
            .email(email)
            .nickname(signupMemberDTO.nickname())
            .profileImageUrl(signupMemberDTO.profileImageUrl())
            .gender(signupMemberDTO.gender())
            .ageGroup(signupMemberDTO.ageGroup())
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

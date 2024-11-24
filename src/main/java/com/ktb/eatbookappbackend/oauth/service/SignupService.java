package com.ktb.eatbookappbackend.oauth.service;

import com.ktb.eatbookappbackend.domain.member.repository.MemberRepository;
import com.ktb.eatbookappbackend.entity.Member;
import com.ktb.eatbookappbackend.entity.constant.AgeGroup;
import com.ktb.eatbookappbackend.entity.constant.Gender;
import com.ktb.eatbookappbackend.oauth.exception.SignupException;
import com.ktb.eatbookappbackend.oauth.message.SignupErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SignupService {

    private final MemberRepository memberRepository;

    public Member createMember(String email, String nickname, String profileImageUrl, Gender gender, AgeGroup ageGroup) {
        if (memberRepository.existsByEmail(email)) {
            throw new SignupException(SignupErrorCode.EMAIL_DUPLICATED);
        }

        Member member = Member.builder()
            .email(email)
            .nickname(nickname)
            .profileImageUrl(profileImageUrl)
            .gender(gender)
            .ageGroup(ageGroup)
            .build();

        return memberRepository.save(member);
    }
}

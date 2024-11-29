package com.ktb.eatbookappbackend.oauth.jwt;

import com.ktb.eatbookappbackend.domain.member.exception.MemberException;
import com.ktb.eatbookappbackend.domain.member.message.MemberErrorCode;
import com.ktb.eatbookappbackend.domain.member.repository.MemberRepository;
import com.ktb.eatbookappbackend.domain.refreshToken.exception.RefreshTokenException;
import com.ktb.eatbookappbackend.domain.refreshToken.message.RefreshTokenErrorCode;
import com.ktb.eatbookappbackend.domain.refreshToken.repository.RefreshTokenRepository;
import com.ktb.eatbookappbackend.entity.Member;
import com.ktb.eatbookappbackend.entity.RefreshToken;
import com.ktb.eatbookappbackend.entity.constant.Role;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;
    private final CookieService cookieService;

    public String renewToken(HttpServletResponse response, String refreshToken) {
        String renewAccessToken = createAccessToken(refreshToken);
        response.setHeader("Authorization", "Bearer " + renewAccessToken);
        return renewAccessToken;
    }

    public String createAccessToken(final String refreshToken) {
        RefreshToken findRefreshToken = refreshTokenRepository.findById(refreshToken)
            .orElseThrow(() -> new RefreshTokenException(RefreshTokenErrorCode.TOKEN_NOT_FOUND));

        Member member = findRefreshToken.getMember();
        return jwtUtil.generateAccessToken(member.getId(), Role.MEMBER);
    }

    public void saveRefreshToken(String refreshToken, String memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));
        refreshTokenRepository.save(new RefreshToken(refreshToken, member));
    }

    public void deleteRefreshToken(String refreshToken) {
        refreshTokenRepository.deleteById(refreshToken);
    }
}

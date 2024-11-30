package com.ktb.eatbookappbackend.oauth.jwt;

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
    private final JwtUtil jwtUtil;

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

    public void deleteRefreshToken(String refreshToken) {
        refreshTokenRepository.deleteById(refreshToken);
    }
}

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

    /**
     * 리프레시 토큰을 사용하여 액세스 토큰을 갱신하는 역할을 담당합니다. 새로운 액세스 토큰을 생성하고, 응답 헤더에 설정한 다음, 새로운 액세스 토큰을 반환합니다.
     *
     * @param response     HttpServletResponse 객체로, 응답 헤더를 설정합니다.
     * @param refreshToken 새로운 액세스 토큰을 생성하는데 사용되는 리프레시 토큰.
     * @return 새로 생성된 액세스 토큰.
     */
    public String renewToken(HttpServletResponse response, String refreshToken) {
        String renewAccessToken = createAccessToken(refreshToken);
        response.setHeader("Authorization", "Bearer " + renewAccessToken);
        return renewAccessToken;
    }

    /**
     * 제공된 리프레시 토큰을 사용하여 새로운 액세스 토큰을 생성합니다.
     *
     * @param refreshToken 새로운 액세스 토큰을 생성하는데 사용되는 리프레시 토큰.
     * @return 새로 생성된 액세스 토큰.
     * @throws RefreshTokenException 제공된 리프레시 토큰이 데이터베이스에서 찾을 수 없는 경우.
     */
    public String createAccessToken(final String refreshToken) {
        RefreshToken findRefreshToken = refreshTokenRepository.findById(refreshToken)
            .orElseThrow(() -> new RefreshTokenException(RefreshTokenErrorCode.TOKEN_NOT_FOUND));
    
        Member member = findRefreshToken.getMember();
        return jwtUtil.generateAccessToken(member.getId(), Role.MEMBER);
    }

    /**
     * 제공된 리프레시 토큰을 데이터베이스에서 삭제합니다.
     * 이 메서드는 유효한 리프레시 토큰만 삭제합니다.
     *
     * @param refreshToken 삭제할 리프레시 토큰.
     *                     null이거나 유효하지 않은 경우, 아무 작업도 수행하지 않습니다.
     */
    public void deleteRefreshToken(String refreshToken) {
        if (refreshToken != null && jwtUtil.validateRefreshToken(refreshToken)) {
            refreshTokenRepository.deleteById(refreshToken);
        }
    }
}

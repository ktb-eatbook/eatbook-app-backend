package com.ktb.eatbookappbackend.oauth.controller;

import static com.ktb.eatbookappbackend.oauth.jwt.constant.TokenType.ACCESS_TOKEN;
import static com.ktb.eatbookappbackend.oauth.jwt.constant.TokenType.REFRESH_TOKEN;

import com.ktb.eatbookappbackend.domain.member.repository.MemberRepository;
import com.ktb.eatbookappbackend.entity.Member;
import com.ktb.eatbookappbackend.global.util.AESUtil;
import com.ktb.eatbookappbackend.oauth.dto.EmailLoginRequestDTO;
import com.ktb.eatbookappbackend.oauth.jwt.JwtUtil;
import com.ktb.eatbookappbackend.oauth.jwt.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/email-login")
public class LoginController {

    private final JwtUtil jwtUtil;
    private final AESUtil aesUtil;
    private final MemberRepository memberRepository;
    private final TokenService tokenService;

    @PostMapping()
    public ResponseEntity<?> processEmailLogin(@RequestBody EmailLoginRequestDTO emailLoginRequestDTO) {
        try {
            String encryptedEmail = emailLoginRequestDTO.encryptedEmail();
            log.info("DTO로 들어온 encryptedEmail Email: {}", encryptedEmail);
            String email = aesUtil.decrypt(encryptedEmail);
            log.info("Decrypted Email: {}", email);

            // 이메일로 사용자 조회 및 처리
            Member member = memberRepository.findByEmail(email).orElse(null);
            if (member == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("사용자를 찾을 수 없습니다.");
            }

            // AccessToken/RefreshToken 생성 및 쿠키 설정
            String accessToken = jwtUtil.generateAccessToken(member.getId(), member.getRole());
            String refreshToken = jwtUtil.generateRefreshToken(member.getId());
            tokenService.saveRefreshToken(refreshToken, member.getId());

            HttpHeaders headers = new HttpHeaders();
            headers.add(ACCESS_TOKEN.getValue(), accessToken);
            headers.add(REFRESH_TOKEN.getValue(), refreshToken);

            log.info("로그인에서 생성한 access token " + accessToken);
            log.info("로그인에서 생성한 refresh token " + refreshToken);

            // 응답 헤더를 노출하도록 설정
            headers.add("Access-Control-Expose-Headers", String.join(", ", ACCESS_TOKEN.getValue(), REFRESH_TOKEN.getValue()));

            return ResponseEntity.ok().headers(headers).body("로그인 성공");
        } catch (Exception e) {
            log.error("로그인 처리 중 오류 발생:", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("로그인 실패");
        }
    }
}

package com.ktb.eatbookappbackend.oauth.controller;

import static com.ktb.eatbookappbackend.oauth.jwt.constant.TokenType.ACCESS_TOKEN;
import static com.ktb.eatbookappbackend.oauth.jwt.constant.TokenType.REFRESH_TOKEN;

import com.ktb.eatbookappbackend.domain.member.exception.MemberException;
import com.ktb.eatbookappbackend.domain.member.message.MemberErrorCode;
import com.ktb.eatbookappbackend.domain.member.repository.MemberRepository;
import com.ktb.eatbookappbackend.domain.member.service.MemberService;
import com.ktb.eatbookappbackend.domain.refreshToken.repository.RefreshTokenRepository;
import com.ktb.eatbookappbackend.entity.Member;
import com.ktb.eatbookappbackend.entity.constant.Role;
import com.ktb.eatbookappbackend.global.reponse.SuccessResponse;
import com.ktb.eatbookappbackend.global.reponse.SuccessResponseDTO;
import com.ktb.eatbookappbackend.global.util.AESUtil;
import com.ktb.eatbookappbackend.oauth.dto.EmailLoginRequestDTO;
import com.ktb.eatbookappbackend.oauth.dto.SignupRequestDTO;
import com.ktb.eatbookappbackend.oauth.dto.SignupResponseDTO;
import com.ktb.eatbookappbackend.oauth.jwt.CookieService;
import com.ktb.eatbookappbackend.oauth.jwt.JwtUtil;
import com.ktb.eatbookappbackend.oauth.jwt.TokenService;
import com.ktb.eatbookappbackend.oauth.message.AuthSuccessCode;
import com.ktb.eatbookappbackend.oauth.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AuthController {

    private final JwtUtil jwtUtil;
    private final AESUtil aesUtil;
    private final TokenService tokenService;
    private final CookieService cookieService;
    private final AuthService authService;
    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final MemberService memberService;

    @PostMapping("/email-login")
    public ResponseEntity<SuccessResponseDTO> processEmailLogin(@RequestBody EmailLoginRequestDTO emailLoginRequestDTO) {
        String encryptedEmail = emailLoginRequestDTO.encryptedEmail();
        log.info("DTO로 들어온 encryptedEmail Email: {}", encryptedEmail);
        String email = aesUtil.decrypt(encryptedEmail);
        log.info("Decrypted Email: {}", email);

        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));

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

        return SuccessResponse.toResponseEntity(AuthSuccessCode.LOGIN_COMPLETED, null, headers);
    }

    @PostMapping("/signup")
    public ResponseEntity<SuccessResponseDTO> getSignupInfo(@RequestBody SignupRequestDTO request) {
        String signupToken = request.token();

        if (!jwtUtil.validateSignupToken(signupToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        Map<String, String> signupInfo = jwtUtil.extractSignupClaims(signupToken);
        SignupResponseDTO signupResponseDTO = authService.signUp(
            signupInfo.get("email"),
            signupInfo.get("nickname"),
            signupInfo.get("profileImage"),
            request.gender(),
            request.getAgeGroupEnum()
        );

        String newMemberId = signupResponseDTO.member().id();
        String accessToken = jwtUtil.generateAccessToken(newMemberId, Role.MEMBER);
        String refreshToken = jwtUtil.generateRefreshToken(newMemberId);

        HttpHeaders headers = new HttpHeaders();
        headers.add(ACCESS_TOKEN.getValue(), accessToken);
        headers.add(REFRESH_TOKEN.getValue(), refreshToken);
        headers.add("Access-Control-Expose-Headers", String.join(", ", ACCESS_TOKEN.getValue(), REFRESH_TOKEN.getValue()));

        return SuccessResponse.toResponseEntity(AuthSuccessCode.SIGN_UP_COMPLETED, signupResponseDTO, headers);
    }

    @DeleteMapping("/logout")
    public ResponseEntity<SuccessResponseDTO> logout(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();

        String accessToken = cookieService.extractCookie(cookies, ACCESS_TOKEN.getValue());
        String refreshToken = cookieService.extractCookie(cookies, REFRESH_TOKEN.getValue());

        if (accessToken != null) {
            cookieService.deleteCookie(response, ACCESS_TOKEN.getValue());
        }

        if (refreshToken != null && jwtUtil.validateRefreshToken(refreshToken)) {
            refreshTokenRepository.deleteById(refreshToken);
            cookieService.deleteCookie(response, REFRESH_TOKEN.getValue());
        }

        return SuccessResponse.toResponseEntity(AuthSuccessCode.LOGOUT_COMPLETED);
    }

    @Secured(Role.MEMBER_VALUE)
    @DeleteMapping("/members")
    public ResponseEntity<SuccessResponseDTO> deleteMember(
        HttpServletRequest request,
        HttpServletResponse response,
        @AuthenticationPrincipal String memberId
    ) {
        memberService.deleteMember(memberId);

        Cookie[] cookies = request.getCookies();
        String accessToken = cookieService.extractCookie(cookies, ACCESS_TOKEN.getValue());
        String refreshToken = cookieService.extractCookie(cookies, REFRESH_TOKEN.getValue());

        if (accessToken != null) {
            cookieService.deleteCookie(response, ACCESS_TOKEN.getValue());
        }

        if (refreshToken != null && jwtUtil.validateRefreshToken(refreshToken)) {
            refreshTokenRepository.deleteById(refreshToken);
            cookieService.deleteCookie(response, REFRESH_TOKEN.getValue());
        }
        return SuccessResponse.toResponseEntity(AuthSuccessCode.DELETE_MEMBER_COMPLETED);
    }
}

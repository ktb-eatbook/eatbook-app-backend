package com.ktb.eatbookappbackend.oauth.controller;

import static com.ktb.eatbookappbackend.oauth.jwt.constant.TokenType.ACCESS_TOKEN;
import static com.ktb.eatbookappbackend.oauth.jwt.constant.TokenType.REFRESH_TOKEN;

import com.ktb.eatbookappbackend.domain.member.exception.MemberException;
import com.ktb.eatbookappbackend.domain.member.message.MemberErrorCode;
import com.ktb.eatbookappbackend.domain.member.repository.MemberRepository;
import com.ktb.eatbookappbackend.domain.member.service.MemberService;
import com.ktb.eatbookappbackend.entity.Member;
import com.ktb.eatbookappbackend.entity.constant.Role;
import com.ktb.eatbookappbackend.global.reponse.SuccessResponse;
import com.ktb.eatbookappbackend.global.reponse.SuccessResponseDTO;
import com.ktb.eatbookappbackend.global.util.AESUtil;
import com.ktb.eatbookappbackend.oauth.dto.EmailLoginRequestDTO;
import com.ktb.eatbookappbackend.oauth.dto.MemberDTO;
import com.ktb.eatbookappbackend.oauth.dto.SignupRequestDTO;
import com.ktb.eatbookappbackend.oauth.dto.SignupResponseDTO;
import com.ktb.eatbookappbackend.oauth.exception.SignupException;
import com.ktb.eatbookappbackend.oauth.jwt.JwtUtil;
import com.ktb.eatbookappbackend.oauth.jwt.TokenService;
import com.ktb.eatbookappbackend.oauth.message.AuthSuccessCode;
import com.ktb.eatbookappbackend.oauth.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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
    private final AuthService authService;
    private final MemberRepository memberRepository;
    private final MemberService memberService;

    /**
     * 이메일 로그인 요청을 처리하고 액세스 토큰과 리프레시 토큰을 반환합니다.
     *
     * @param emailLoginRequestDTO 이메일이 암호화된 상태로 포함된 요청 객체입니다.
     * @return 이메일 로그인 성공에 대한 응답으로, SuccessResponseDTO에 액세스 토큰과 리프레시 토큰이 포함되어 있는 ResponseEntity를 반환합니다.
     * @throws MemberException 이메일에 해당하는 멤버를 찾을 수 없는 경우 발생합니다.
     */
    @PostMapping("/email-login")
    public ResponseEntity<SuccessResponseDTO> processEmailLogin(@RequestBody EmailLoginRequestDTO emailLoginRequestDTO) {
        String encryptedEmail = emailLoginRequestDTO.encryptedEmail();
        String email = aesUtil.decrypt(encryptedEmail);

        Member member = memberRepository.findByEmailAndDeletedAtIsNull(email)
            .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));

        MemberDTO memberDTO = authService.login(member);

        String accessToken = jwtUtil.generateAccessToken(member.getId(), member.getRole());
        String refreshToken = jwtUtil.generateRefreshToken(member.getId());

        HttpHeaders headers = new HttpHeaders();
        headers.add(ACCESS_TOKEN.getValue(), accessToken);
        headers.add(REFRESH_TOKEN.getValue(), refreshToken);

        // 응답 헤더를 노출하도록 설정
        headers.add("Access-Control-Expose-Headers", String.join(", ", ACCESS_TOKEN.getValue(), REFRESH_TOKEN.getValue()));

        return SuccessResponse.toResponseEntity(AuthSuccessCode.LOGIN_COMPLETED, memberDTO, headers);
    }

    /**
     * 회원 가입 요청을 처리하고, 회원 가입에 성공하면 액세스 토큰과 리프레시 토큰을 반환합니다.
     *
     * @param request 회원 가입 요청 DTO.
     * @return {@link ResponseEntity}로, 성공적으로 회원 가입이 이루어지면 {@link SuccessResponseDTO}에 액세스 토큰, 리프레시 토큰, {@link SignupResponseDTO}가 포함되어
     * 있습니다. HTTP 헤더에 'Access-Control-Expose-Headers'를 추가하여 응답 헤더를 노출합니다.
     * @throws SignupException {@link SignupRequestDTO}에 유효하지 않은 토큰이 포함된 경우 발생합니다.
     */
    @PostMapping("/signup")
    public ResponseEntity<SuccessResponseDTO> getSignupInfo(@RequestBody SignupRequestDTO request) {
        SignupResponseDTO signupResponseDTO = authService.signUp(request);

        String newMemberId = signupResponseDTO.member().id();
        String accessToken = jwtUtil.generateAccessToken(newMemberId, Role.MEMBER);
        String refreshToken = jwtUtil.generateRefreshToken(newMemberId);

        HttpHeaders headers = new HttpHeaders();
        headers.add(ACCESS_TOKEN.getValue(), accessToken);
        headers.add(REFRESH_TOKEN.getValue(), refreshToken);
        headers.add("Access-Control-Expose-Headers", String.join(", ", ACCESS_TOKEN.getValue(), REFRESH_TOKEN.getValue()));

        return SuccessResponse.toResponseEntity(AuthSuccessCode.SIGN_UP_COMPLETED, signupResponseDTO, headers);
    }

    /**
     * 로그아웃 요청을 처리합니다.
     * <p>
     * 이 메서드는 HTTP 요청으로부터 리프레시 토큰을 추출하고, {@link TokenService}를 사용하여 해당 리프레시 토큰을 삭제합니다. 이후 로그아웃 성공 메시지를 {@link SuccessResponse}로 반환합니다.
     *
     * @param request HTTP 요청.
     * @return {@link ResponseEntity}로, 성공적으로 로그아웃이 이루어지면 {@link SuccessResponseDTO}에 로그아웃 성공 메시지가 포함되어 있습니다.
     */
    @DeleteMapping("/logout")
    public ResponseEntity<SuccessResponseDTO> logout(HttpServletRequest request) {
        String refreshToken = request.getHeader(REFRESH_TOKEN.getValue());
        tokenService.deleteRefreshToken(refreshToken);
        return SuccessResponse.toResponseEntity(AuthSuccessCode.LOGOUT_COMPLETED);
    }

    /**
     * 인증된 멤버만 호출할 수 있도록 보호되는 HTTP DELETE 요청 메서드로, 지정된 멤버 ID와 연결된 멤버를 삭제하고 로그아웃합니다.
     * <p>
     * HTTP 요청과 멤버 ID를 매개변수로 받습니다. 이 메서드는 {@link MemberService#deleteMember(String)} 메서드를 호출하여 지정된 ID를 가진 멤버를 삭제합니다. 멤버를 삭제한 후, HTTP 요청 헤더에서
     * 리프레시 토큰을 추출하고 {@link TokenService#deleteRefreshToken(String)} 메서드를 사용하여 해당 리프레시 토큰을 삭제합니다. 마지막으로, 성공 응답을 반환하며,
     * {@link AuthSuccessCode#DELETE_MEMBER_COMPLETED} 코드를 가진 성공 응답을 포함합니다.
     *
     * @param request  멤버 ID를 삭제할 HTTP 요청.
     * @param memberId 삭제할 멤버의 ID.
     * @return {@link ResponseEntity}로, {@link AuthSuccessCode#DELETE_MEMBER_COMPLETED} 코드를 가진 성공 응답을 포함합니다.
     */
    @Secured(Role.MEMBER_VALUE)
    @DeleteMapping("/members")
    public ResponseEntity<SuccessResponseDTO> deleteMember(
        HttpServletRequest request,
        @AuthenticationPrincipal String memberId
    ) {
        memberService.deleteMember(memberId);
        String refreshToken = request.getHeader(REFRESH_TOKEN.getValue());
        tokenService.deleteRefreshToken(refreshToken);
        return SuccessResponse.toResponseEntity(AuthSuccessCode.DELETE_MEMBER_COMPLETED);
    }

    /**
     * 토큰 유효성 확인 API
     * <p>
     * 이 API는 JWT 필터를 통해 토큰이 유효한 경우 OK 상태를 반환합니다.
     *
     * @return {@link ResponseEntity}로, 성공적으로 토큰이 유효한 경우 OK 상태와 메시지를 반환합니다.
     */
    @Secured(Role.MEMBER_VALUE)
    @GetMapping("/validate-token")
    public ResponseEntity<SuccessResponseDTO> validateToken() {
        return SuccessResponse.toResponseEntity(AuthSuccessCode.TOKEN_VALID);
    }
}

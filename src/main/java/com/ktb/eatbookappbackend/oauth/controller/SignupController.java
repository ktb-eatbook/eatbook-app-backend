package com.ktb.eatbookappbackend.oauth.controller;

import static com.ktb.eatbookappbackend.oauth.jwt.constant.TokenType.ACCESS_TOKEN;
import static com.ktb.eatbookappbackend.oauth.jwt.constant.TokenType.REFRESH_TOKEN;

import com.ktb.eatbookappbackend.entity.constant.Role;
import com.ktb.eatbookappbackend.global.reponse.SuccessResponse;
import com.ktb.eatbookappbackend.global.reponse.SuccessResponseDTO;
import com.ktb.eatbookappbackend.oauth.dto.SignupRequestDTO;
import com.ktb.eatbookappbackend.oauth.dto.SignupResponseDTO;
import com.ktb.eatbookappbackend.oauth.jwt.JwtUtil;
import com.ktb.eatbookappbackend.oauth.message.SignupSuccessCode;
import com.ktb.eatbookappbackend.oauth.service.SignupService;
import java.util.Map;
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
@RequestMapping("/api/signup")
public class SignupController {

    private final JwtUtil jwtUtil;
    private final SignupService signupService;

    @PostMapping()
    public ResponseEntity<SuccessResponseDTO> getSignupInfo(@RequestBody SignupRequestDTO request) {
        String signupToken = request.token();

        if (!jwtUtil.validateSignupToken(signupToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        Map<String, String> signupInfo = jwtUtil.extractSignupClaims(signupToken);
        SignupResponseDTO signupResponseDTO = signupService.signUp(
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

        return SuccessResponse.toResponseEntity(SignupSuccessCode.SIGN_UP_COMPLETED, signupResponseDTO, headers);
    }
}

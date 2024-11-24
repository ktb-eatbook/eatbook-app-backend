package com.ktb.eatbookappbackend.oauth.controller;

import com.ktb.eatbookappbackend.entity.Member;
import com.ktb.eatbookappbackend.oauth.dto.SignupRequestDTO;
import com.ktb.eatbookappbackend.oauth.dto.SignupResponseDTO;
import com.ktb.eatbookappbackend.oauth.jwt.JwtUtil;
import com.ktb.eatbookappbackend.oauth.service.SignupService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class SignupController {

    private final JwtUtil jwtUtil;
    private final SignupService signupService;

    @PostMapping("/signup")
    public ResponseEntity<SignupResponseDTO> getSignupInfo(@RequestBody SignupRequestDTO request) {
        String token = request.token();

        if (!jwtUtil.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        Map<String, String> signupInfo = jwtUtil.extractSignupClaims(token);
        log.info("signupInfo: " + signupInfo);
        Member newMember = signupService.createMember(
            signupInfo.get("email"),
            signupInfo.get("nickname"),
            signupInfo.get("profileImage"),
            request.gender(),
            request.getAgeGroupEnum()
        );

        SignupResponseDTO responseDTO = new SignupResponseDTO(
            newMember.getId(),
            newMember.getNickname(),
            newMember.getProfileImageUrl(),
            newMember.getEmail()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }
}

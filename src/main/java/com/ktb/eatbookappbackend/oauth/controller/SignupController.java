package com.ktb.eatbookappbackend.oauth.controller;

import static com.ktb.eatbookappbackend.oauth.jwt.constant.TokenType.ACCESS_TOKEN;
import static com.ktb.eatbookappbackend.oauth.jwt.constant.TokenType.REFRESH_TOKEN;
import static org.springframework.http.HttpHeaders.SET_COOKIE;

import com.ktb.eatbookappbackend.entity.Member;
import com.ktb.eatbookappbackend.oauth.dto.SignupRequestDTO;
import com.ktb.eatbookappbackend.oauth.dto.SignupResponseDTO;
import com.ktb.eatbookappbackend.oauth.jwt.CookieService;
import com.ktb.eatbookappbackend.oauth.jwt.JwtUtil;
import com.ktb.eatbookappbackend.oauth.service.SignupService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/signup")
public class SignupController {

    private final JwtUtil jwtUtil;
    private final SignupService signupService;
    private final CookieService cookieService;

    @GetMapping("/additional-info")
    public String loginResult(@RequestParam("token") String token) {
        String state = "SIGN_UP";
        return String.format("""
            <html>
              <body>
                <script>
                  window.flutter_inappwebview.callHandler('loginResult', { state: '%s', token: '%s' });
                </script>
                <p>로그인 처리 중</p>
              </body>
            </html>
            """, state, token);
    }

    @PostMapping()
    public ResponseEntity<SignupResponseDTO> getSignupInfo(@RequestBody SignupRequestDTO request) {
        String token = request.token();

        if (!jwtUtil.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        Map<String, String> signupInfo = jwtUtil.extractSignupClaims(token);
        Member newMember = signupService.createMember(
            signupInfo.get("email"),
            signupInfo.get("nickname"),
            signupInfo.get("profileImage"),
            request.gender(),
            request.getAgeGroupEnum()
        );

        SignupResponseDTO signupResponseDTO = new SignupResponseDTO(
            newMember.getId(),
            newMember.getNickname(),
            newMember.getProfileImageUrl(),
            newMember.getEmail()
        );

        String accessToken = jwtUtil.generateAccessToken(newMember.getId(), newMember.getRole());
        String refreshToken = jwtUtil.generateRefreshToken(newMember.getId());
        ResponseCookie accessTokenCookie = cookieService.createCookie(ACCESS_TOKEN.getValue(), accessToken);
        ResponseCookie refreshTokenCookie = cookieService.createCookie(REFRESH_TOKEN.getValue(), refreshToken);

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .header(SET_COOKIE, accessTokenCookie.toString())
            .header(SET_COOKIE, refreshTokenCookie.toString())
            .body(signupResponseDTO);
    }
}

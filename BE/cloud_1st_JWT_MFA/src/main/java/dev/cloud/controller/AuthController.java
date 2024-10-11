package dev.cloud.controller;

import dev.cloud.dto.*;
import dev.cloud.model.Member;
import dev.cloud.service.AuthService;
import dev.cloud.service.EmailService;
import dev.cloud.service.MemberService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final EmailService emailService;
    private final MemberService memberService;

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody MemberDTO memberDTO) {
        MemberDTO mberDTO = authService.signup(memberDTO);

        // 성공 가입
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signin(@RequestBody MemberDTO memberDTO) {
        // 로그인
        if (authService.login(memberDTO)) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new EmailAuthResponseDto(true, "이메일에서 인증번호를 확인해주세요"));
        }
        // 로그인 실패 응답
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new EmailAuthResponseDto(false, "비밀번호를 확인해주세요"));
    }

    // 2차 인증
    @PostMapping("/smtp")
    public ResponseEntity<?> login(@RequestBody AuthRequestDto authRequestDto, HttpServletResponse response) {
        // 2차 인증 성공 시 토큰 발급
        TokenDto tokenDto = authService.smtpMfa(authRequestDto.email(), authRequestDto.authCode());

        // Refresh Token을 HttpOnly 쿠키로 설정
        Cookie refreshTokenCookie = new Cookie("refreshToken", tokenDto.refreshToken());
        refreshTokenCookie.setHttpOnly(true);   // HttpOnly로 설정하여 XSS 방지
//        refreshTokenCookie.setSecure(true);     // HTTPS에서만 전송
        refreshTokenCookie.setPath("/");        // 전체 경로에서 사용 가능
        refreshTokenCookie.setMaxAge(7 * 24 * 60 * 60); // 7일 동안 유효

        response.addCookie(refreshTokenCookie); // 쿠키에 추가

        // accessToken만 바디로 반환
        return ResponseEntity.ok(tokenDto);
    }

//    @PostMapping("/logout")
//    public void logout(@RequestHeader("Authorization") String accessToken){
//        authService.logout(accessToken);
//    }


}




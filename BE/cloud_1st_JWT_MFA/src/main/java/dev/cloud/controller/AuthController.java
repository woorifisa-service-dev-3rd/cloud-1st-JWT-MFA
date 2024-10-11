package dev.cloud.controller;

import dev.cloud.dto.*;
import dev.cloud.model.Member;
import dev.cloud.service.AuthService;
import dev.cloud.service.EmailService;
import dev.cloud.service.MemberService;
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
                    .body(new EmailAuthResponseDto(true, "이메일을 확인해주세요"));
        }
        // 로그인 실패 응답
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new EmailAuthResponseDto(false, "비밀번호를 확인해주세요"));
    }

    // 2차 인증
    @PostMapping("/smtp")
    public ResponseEntity<?> login(@RequestBody AuthRequestDto authRequestDto) {
        TokenDto JWT = authService.smtpMfa(authRequestDto.email(), authRequestDto.authCode());
        if (JWT == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(JWT, HttpStatus.OK);
    }


}




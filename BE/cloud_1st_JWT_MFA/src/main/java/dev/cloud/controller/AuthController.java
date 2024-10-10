package dev.cloud.controller;

import dev.cloud.dto.EmailAuthResponseDto;
import dev.cloud.dto.SigninResponseDto;
import dev.cloud.model.User;
import dev.cloud.record.UserDTO;
import dev.cloud.service.EmailService;
import dev.cloud.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final EmailService emailService;
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody UserDTO userDTO) {
        // 회원가입 요청 변환 및 저장
        User newUser = userService.signUp(userDTO.toEntity());
        // 기본 응답 반환
        return ResponseEntity.ok(UserDTO.basicResponse(newUser));
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signin(@RequestBody UserDTO userDTO) {
        // 로그인
        if (userService.signin(userDTO.toEntity())) {
            EmailAuthResponseDto emailResponse = emailService.sendEmail(userDTO.email());
            SigninResponseDto response = new SigninResponseDto(
                    "이메일 인증 코드를 입력하세요.",
                    userDTO.email(),
                    emailResponse.isSuccess()
            );
            return ResponseEntity.ok(response);
        }
        // 로그인 실패 응답
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new EmailAuthResponseDto(false, "비밀번호를 확인해주세요"));
    }

    // 인증번호 검증
    @PostMapping(value = "/auth", produces = "application/json")
    public ResponseEntity<?> checkAuthCode(@RequestParam String address, @RequestParam String authCode) {
        boolean isVerified = emailService.validateAuthCode(address, authCode).isSuccess();

        if (isVerified) { // 인증번호 맞춤
            // 사용자 검색
            // JWT 발급
            return new ResponseEntity<>(HttpStatus.OK);
        }
        // 인증번호 틀림
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new EmailAuthResponseDto(false, "인증 코드가 일치하지 않습니다."));
    }
}




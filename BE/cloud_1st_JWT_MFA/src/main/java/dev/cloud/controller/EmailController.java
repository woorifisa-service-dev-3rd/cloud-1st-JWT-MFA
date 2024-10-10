package dev.cloud.controller;

import dev.cloud.dto.EmailAuthResponseDto;
import dev.cloud.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/email")
public class EmailController {

    private final EmailService emailService;

    // 인증번호 전송
    @GetMapping(value = "/auth", produces = "application/json")
    public EmailAuthResponseDto sendAuthCode(@RequestParam String address) {
        return emailService.sendEmail(address);
    }

    // 인증번호 검증
    @PostMapping(value = "/auth", produces = "application/json")
    public EmailAuthResponseDto checkAuthCode(@RequestParam String address, @RequestParam String authCode) {
        return emailService.validateAuthCode(address, authCode);
    }
}

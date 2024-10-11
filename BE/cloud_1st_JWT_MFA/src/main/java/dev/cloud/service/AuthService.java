package dev.cloud.service;

import dev.cloud.dto.*;
import dev.cloud.jwt.TokenProvider;
import dev.cloud.model.Member;
import dev.cloud.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final EmailService emailService;

    @Transactional
    public MemberDTO signup(MemberDTO memberDTO) {
        if (memberRepository.existsByEmail(memberDTO.email())) {
            throw new RuntimeException("이미 가입되어 있는 유저입니다.");
        }
        // 비번해싱
        Member user = memberDTO.toMember(passwordEncoder);
        return MemberDTO.of(memberRepository.save(user));
    }


    // 초기 로그인: 비밀번호 인증 후 이메일로 인증 코드 전송
    public boolean login(MemberDTO memberDTO) {
        try {
            UsernamePasswordAuthenticationToken authenticationToken = memberDTO.toAuthentication();
            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

            // 비밀번호 인증 성공 시 이메일로 인증 코드 전송
            emailService.sendEmail(memberDTO.email());
            return true;
        } catch (AuthenticationException e) {
            // 인증 실패 처리
            System.out.println("로그인 실패: " + e.getMessage());
            return false;
        }
    }

    // 클라이언트로부터 인증 코드 입력받아 JWT 발급
    public TokenDto smtpMfa(String email, String authCode) {
        EmailAuthResponseDto EmailAuthDTO = emailService.validateAuthCode(email, authCode);
        if (EmailAuthDTO.isSuccess()) {
            // 인증 성공 시 토큰 발급
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            return tokenProvider.generateTokenDto(authentication);
        } else {
            throw new RuntimeException("Invalid verification code");
        }
    }
}

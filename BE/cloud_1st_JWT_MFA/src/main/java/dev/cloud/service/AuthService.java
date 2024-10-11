package dev.cloud.service;

import dev.cloud.dto.*;
import dev.cloud.jwt.TokenProvider;
import dev.cloud.model.IpLog;
import dev.cloud.model.Member;
import dev.cloud.repository.IpLogRepository;
import dev.cloud.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final EmailService emailService;
    private final IpLogService ipLogService;

    @Transactional
    public MemberDTO signup(MemberDTO memberDTO) {
        if (memberRepository.existsByEmail(memberDTO.email())) {
            throw new RuntimeException("이미 가입되어 있는 유저입니다.");
        }
        // 비번해싱
        Member user = memberDTO.toMember(passwordEncoder);
        return MemberDTO.of(memberRepository.save(user));
    }

    // 1. 비밀번호 인증 메서드
    private Authentication authenticateUser(MemberDTO memberDTO) {
        UsernamePasswordAuthenticationToken authenticationToken = memberDTO.toAuthentication();
        return authenticationManagerBuilder.getObject().authenticate(authenticationToken);
    }

    // 2. IP 확인 및 저장 메서드
    private boolean checkAndSaveIp(MemberDTO memberDTO, String memberIp) {
        Optional<Member> findmember = memberRepository.findByEmail(memberDTO.email());
        log.info("있냐? {}", findmember);
        if (findmember.isPresent()) {
            return ipLogService.checkAndSave(findmember.get(), memberIp);
        }
        return false;
    }

    // 3. 이메일 인증 요청 메서드
    private void requestEmailVerification(String email) {
        emailService.sendEmail(email);
    }

    // 4. JWT 발급 메서드
    private TokenDto generateJwt(Authentication authentication) {
        return tokenProvider.generateTokenDto(authentication);
    }

    // 로그인 메서드 - 분리된 메서드들을 호출하여 로그인 처리
    public LoginResultDTO login(SignInRequestDto signInRequestDto) {
        try {
            System.out.println(signInRequestDto.memberIp());
            // 추출하여 사용
            String memberIp = signInRequestDto.memberIp();
            MemberDTO memberDTO = MemberDTO.fromSignInRequest(signInRequestDto);

            Authentication authentication = authenticateUser(memberDTO);
            // IP 체크
            if (checkAndSaveIp(memberDTO, memberIp)) {
                // IP 확인 완료 -> JWT 발급 후 로그인 완료
                TokenDto token = generateJwt(authentication);
                return new LoginResultDTO(true, token, "SAME_ADDRESS");
            } else {
                // 새로운 IP -> 이메일 인증 요청
                requestEmailVerification(memberDTO.email());
                return new LoginResultDTO(false, null, "EMAIL_VERIFICATION_REQUIRED");
            }
        } catch (AuthenticationException e) {
            return new LoginResultDTO(false, null, "AUTHENTICATION_FAILED");
        }
    }

    // 2차 인증 코드 확인 및 JWT 발급
    public TokenDto smtpMfa(String email, String authCode) {
        System.out.println(authCode);
        EmailAuthResponseDto emailAuthDTO = emailService.validateAuthCode(email, authCode);
        if (emailAuthDTO.isSuccess()) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            return generateJwt(authentication);
        } else {
            throw new RuntimeException("인증코드 메롱임");
        }
    }

}

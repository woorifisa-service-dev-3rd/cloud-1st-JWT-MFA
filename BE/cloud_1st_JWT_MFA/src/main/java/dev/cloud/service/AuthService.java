package dev.cloud.service;

import dev.cloud.dto.TokenDto;
import dev.cloud.dto.MemberRequestDto;
import dev.cloud.dto.MemberResponseDto;
import dev.cloud.dto.TokenRequestDto;
import dev.cloud.jwt.TokenProvider;
import dev.cloud.model.Member;
import dev.cloud.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final RedisTemplate<String, String> redisTemplate;

    @Transactional
    public MemberResponseDto signup(MemberRequestDto memberRequestDto) {
        if (memberRepository.existsByEmail(memberRequestDto.getEmail())) {
            throw new RuntimeException("이미 가입되어 있는 유저입니다.");
        }

        Member user = memberRequestDto.toUser(passwordEncoder);
        return MemberResponseDto.of(memberRepository.save(user));
    }

    @Transactional
    public TokenDto login(MemberRequestDto memberRequestDto) {
        UsernamePasswordAuthenticationToken authenticationToken = memberRequestDto.toAuthentication();

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        redisTemplate.opsForValue().set(
                "RefreshToken:" + authentication.getName(), tokenDto.getRefreshToken(),
                tokenProvider.getRefreshTokenExpireTime(), TimeUnit.MILLISECONDS);

        return tokenDto;
    }

    @Transactional
    public ResponseEntity<?> reissue(TokenRequestDto tokenRequestDto) {
        if (!tokenProvider.validateToken(tokenRequestDto.getRefreshToken())) {
            return ResponseEntity.badRequest().body("Refresh Token이 유효하지 않습니다.");
        }

        Authentication authentication = tokenProvider.getAuthentication(tokenRequestDto.getRefreshToken());

        String refreshToken = (String) redisTemplate.opsForValue().get("RefreshToken: " + authentication.getName());
        if (!refreshToken.equals(tokenRequestDto.getRefreshToken())) {
            return ResponseEntity.badRequest().body("토큰의 유저 정보가 일치하지 않습니다.");
        }

        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        redisTemplate.opsForValue()
                .set("RefreshToken:" + authentication.getName(), tokenDto.getRefreshToken(), tokenDto.getAccessTokenExpiresIn(), TimeUnit.MICROSECONDS);

        return ResponseEntity.ok().body(tokenDto);
    }

    @Transactional
    public ResponseEntity<?> logout(TokenRequestDto tokenRequestDto) {
        if (!tokenProvider.validateToken(tokenRequestDto.getAccessToken())) {
            return ResponseEntity.badRequest().body("유효하지 않은 액세스 토큰입니다.");
        }

        Authentication authentication = tokenProvider.getAuthentication(tokenRequestDto.getAccessToken());


        redisTemplate.opsForValue().set(
                "BlacklistedToken:" + tokenRequestDto.getAccessToken(),
                String.valueOf(true),
                tokenProvider.getAccessTokenExpireTime(),
                TimeUnit.MILLISECONDS
        );

        redisTemplate.delete("RefreshToken:" + authentication.getName());
        return ResponseEntity.ok().body("로그아웃 성공");
    }
}

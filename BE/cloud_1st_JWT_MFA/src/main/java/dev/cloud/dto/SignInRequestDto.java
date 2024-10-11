package dev.cloud.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public record SignInRequestDto(
        @Email(message = "이메일 형식이 올바르지 않습니다.")
        @NotBlank(message = "이메일은 필수 입력 사항입니다.")
        String email,

        @NotBlank(message = "비밀번호는 필수 입력 사항입니다.")
        @Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다.")
        String pw,

        @NotBlank(message = "IP 주소는 필수 입력 사항입니다.")
        String memberIp
) {
    // Authentication을 위한 메서드
    public UsernamePasswordAuthenticationToken toAuthentication() {
        return new UsernamePasswordAuthenticationToken(email, pw);
    }
}

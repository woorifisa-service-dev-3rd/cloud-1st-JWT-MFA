package dev.cloud.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SigninResponseDto {
    private String message;  // 응답 메시지
    private String email;    // 사용자 이메일 (변경 불가로 프론트에 전달)
    private boolean emailSent; // 인증 이메일 발송 성공 여부
}

package dev.cloud.record;

import dev.cloud.model.User;
import jakarta.persistence.Column;

public record UserDTO(
        Long id,
        String email,
        String pw,
        String name
) {
    /* 요청 구간 */
    // 회원 가입 요청 (ID 없이 이메일, 비밀번호, 이름만)
    public static UserDTO signUpRequest(String email, String pw, String name) {
        return new UserDTO(null, email, pw, name);
    }

    // 로그인 요청 (이메일과 비밀번호만)
    public static UserDTO loginRequest(String email, String pw) {
        return new UserDTO(null, email, pw, null);
    }
    /*########################################################################################*/

    /* 응답 구간 */
    // 기본 응답 (비밀번호 없이)
    public static UserDTO basicResponse(User user) {
        return new UserDTO(user.getId(), user.getEmail(), null, user.getName());
    }

    // 상세 응답 (모든 정보 포함)
    public static UserDTO detailedResponse(User user) {
        return new UserDTO(user.getId(), user.getEmail(), user.getPw(), user.getName());
    }
    /*########################################################################################*/

    public User toEntity() {
        return new User(id, email, pw, name);
    }
}

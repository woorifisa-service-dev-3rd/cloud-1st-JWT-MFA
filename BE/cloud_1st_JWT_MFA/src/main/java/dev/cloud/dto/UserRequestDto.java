package dev.cloud.dto;

import dev.cloud.model.Authority;
import dev.cloud.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDto {

    private String email;
    private String pw;
    private String name;

    public User toUser(PasswordEncoder passwordEncoder) {
        return User.builder()
                .email(email)
                .pw(passwordEncoder.encode(pw))
                .name(name)
                .authority(Authority.ROLE_USER)
                .build();
    }

    public UsernamePasswordAuthenticationToken toAuthentication() {
        return new UsernamePasswordAuthenticationToken(email, pw);
    }
}

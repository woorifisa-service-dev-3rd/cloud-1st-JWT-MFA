package dev.cloud.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.cloud.model.Authority;
import dev.cloud.model.Member;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

@Builder
public record MemberDTO(
        Long id,

        @Email(message = "이메일 형식이 올바르지 않습니다.")
        @NotBlank(message = "이메일은 필수 입력 사항입니다.")
        String email,

        @NotBlank(message = "비밀번호는 필수 입력 사항입니다.")
        @Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다.")
        String pw,

        @NotBlank(message = "이름은 필수 입력 사항입니다.")
        String name,

        Authority authority,

        String memberIp
) {

    // 엔티티를 DTO로 변환하는 정적 메소드
    public static MemberDTO fromEntity(Member member) {
        return new MemberDTO(
                member.getId(),
                member.getEmail(),
                member.getPw(),
                member.getName(),
                member.getAuthority()
        );
    }

    // DTO를 엔티티로 변환하는 메소드
    public Member toEntity() {
        Member member = new Member();
        member.setId(this.id);
        member.setEmail(this.email);
        member.setPw(this.pw);
        member.setName(this.name);
        member.setAuthority(this.authority);
        return member;
    }

    // 권한이 ADMIN인지 확인하는 메소드
    public boolean isAdmin() {
        return this.authority == Authority.ROLE_ADMIN;
    }

    // 이메일 도메인을 추출하는 메소드
    public String getEmailDomain() {
        if (this.email != null && this.email.contains("@")) {
            return this.email.substring(this.email.indexOf("@") + 1);
        }
        return "";
    }

    // 기존 DTO를 기반으로 일부 필드만 변경한 새로운 DTO를 생성하는 메소드
    public MemberDTO copyWith(String email, String pw, String name, Authority authority) {
        return new MemberDTO(
                this.id,
                email != null ? email : this.email,
                pw != null ? pw : this.pw,
                name != null ? name : this.name,
                authority != null ? authority : this.authority
        );
    }

    // DTO를 JSON으로 변환하는 메소드
    public String toJson() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(this);
    }

    // JSON을 DTO로 변환하는 메소드
    public static MemberDTO fromJson(String json) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, MemberDTO.class);
    }

    public UsernamePasswordAuthenticationToken toAuthentication() {
        return new UsernamePasswordAuthenticationToken(email, pw);
    }

    public Member toMember(PasswordEncoder passwordEncoder) {
        return Member.builder()
                .email(email)
                .pw(passwordEncoder.encode(pw))
                .name(name)
                .authority(Authority.ROLE_USER)
                .build();
    }


    public static MemberDTO of(Member member) {
        return MemberDTO.builder()
                .id(member.getId())
                .email(member.getEmail())
                .name(member.getName())
                .authority(member.getAuthority())
                .build();
    }
}

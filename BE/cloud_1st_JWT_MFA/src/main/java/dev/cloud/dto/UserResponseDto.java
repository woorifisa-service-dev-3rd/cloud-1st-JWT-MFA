package dev.cloud.dto;

import dev.cloud.model.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserResponseDto {
    private Long id;
    private String email;
    private String name;
    private String authority;

    @Builder
    public UserResponseDto(String authority, String email, Long id, String name) {
        this.authority = authority;
        this.email = email;
        this.id = id;
        this.name = name;
    }

    public static UserResponseDto of(Member member) {
        return UserResponseDto.builder()
                .id(member.getId())
                .email(member.getEmail())
                .name(member.getName())
                .authority(member.getAuthority().name())
                .build();
    }
}

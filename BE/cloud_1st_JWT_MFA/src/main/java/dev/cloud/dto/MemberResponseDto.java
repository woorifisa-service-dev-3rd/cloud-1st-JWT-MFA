package dev.cloud.dto;

import dev.cloud.model.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberResponseDto {
    private Long id;
    private String email;
    private String name;
    private String authority;

    @Builder
    public MemberResponseDto(String authority, String email, Long id, String name) {
        this.authority = authority;
        this.email = email;
        this.id = id;
        this.name = name;
    }

    public static MemberResponseDto of(Member member) {
        return MemberResponseDto.builder()
                .id(member.getId())
                .email(member.getEmail())
                .name(member.getName())
                .authority(member.getAuthority().name())
                .build();
    }
}

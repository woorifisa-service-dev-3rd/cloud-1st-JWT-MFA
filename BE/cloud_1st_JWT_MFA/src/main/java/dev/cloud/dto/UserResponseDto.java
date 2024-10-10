package dev.cloud.dto;

import dev.cloud.model.User;
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

    public static UserResponseDto of(User user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .authority(user.getAuthority().name())
                .build();
    }
}

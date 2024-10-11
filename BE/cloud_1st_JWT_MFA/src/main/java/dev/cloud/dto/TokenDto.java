package dev.cloud.dto;

public record TokenDto(
        String accessToken,
        String refreshToken,
        Long accessTokenExpiresIn
) {}

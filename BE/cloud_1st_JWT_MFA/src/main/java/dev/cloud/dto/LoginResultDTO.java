package dev.cloud.dto;

public record LoginResultDTO(
        boolean isAuthenticated, TokenDto token, String message
) {

    public boolean isSuccessful() {
        return isAuthenticated;
    }

    public boolean hasToken() {
        return token != null;
    }

    public boolean hasMessage() {
        return message != null && !message.isBlank();
    }


}

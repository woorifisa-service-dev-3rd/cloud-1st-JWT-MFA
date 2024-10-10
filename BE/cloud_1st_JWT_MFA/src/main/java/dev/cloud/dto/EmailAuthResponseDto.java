package dev.cloud.dto;

public class EmailAuthResponseDto {
    private boolean success;
    private String responseMessage;

    public EmailAuthResponseDto(boolean success, String responseMessage){
        this.success = success;
        this.responseMessage = responseMessage;
    }
}

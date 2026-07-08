package com.cs.chat_api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SendMessageRequestDto {
    @NotBlank(message = "Content is required")
    private String content;

    @NotBlank(message = "Room name is required")
    private String roomName;

    @NotBlank(message = "Sender username is required")
    private String senderUsername;
}
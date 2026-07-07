package com.cd.chat_api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class MessageResponseDto {
    private Long id;
    private String content;
    private LocalDateTime sentAt;
    private Long roomId;
    private String senderUsername;
}

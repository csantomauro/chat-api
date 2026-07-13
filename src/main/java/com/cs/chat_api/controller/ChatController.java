package com.cs.chat_api.controller;

import com.cs.chat_api.dto.MessageResponseDto;
import com.cs.chat_api.dto.SendMessageRequestDto;
import com.cs.chat_api.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final MessageService messageService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat.sendMessage")
    public void sendMessage(SendMessageRequestDto request) {
        MessageResponseDto savedMessage = messageService.sendMessage(request);

        messagingTemplate.convertAndSend(
                "/topic/room/" + savedMessage.getRoomId(),
                savedMessage
        );
    }
}

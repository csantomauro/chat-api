package com.cs.chat_api.controller;

import com.cs.chat_api.dto.MessageResponseDto;
import com.cs.chat_api.dto.SendMessageRequestDto;
import com.cs.chat_api.service.MessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @PostMapping
    public ResponseEntity<MessageResponseDto> sendMessage(@Valid @RequestBody SendMessageRequestDto request) {
        MessageResponseDto sent = messageService.sendMessage(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(sent);
    }

    @GetMapping("/room/{roomId}")
    public ResponseEntity<List<MessageResponseDto>> getRoomHistory(@PathVariable Long roomId) {
        return ResponseEntity.ok(messageService.getRoomHistory(roomId));
    }
}

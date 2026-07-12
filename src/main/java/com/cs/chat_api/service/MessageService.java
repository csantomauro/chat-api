package com.cs.chat_api.service;

import com.cs.chat_api.dto.MessageResponseDto;
import com.cs.chat_api.dto.SendMessageRequestDto;
import com.cs.chat_api.exception.RoomNotFoundException;
import com.cs.chat_api.exception.UserNotFoundException;
import com.cs.chat_api.model.Message;
import com.cs.chat_api.model.Room;
import com.cs.chat_api.model.User;
import com.cs.chat_api.repository.MessageRepository;
import com.cs.chat_api.repository.RoomRepository;
import com.cs.chat_api.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;

    public MessageResponseDto sendMessage(SendMessageRequestDto request){
        Room room = roomRepository.findByName(request.getRoomName())
                .orElseThrow(() -> new RoomNotFoundException("Room not found: " + request.getRoomName()));

        User user = userRepository.findByUsername(request.getSenderUsername())
                .orElseThrow(() -> new UserNotFoundException("User not found: " + request.getSenderUsername()));

        Message message = new Message();
        message.setContent(request.getContent());
        message.setRoom(room);
        message.setSender(user);

        Message saved =  messageRepository.save(message);
        return toResponseDto(saved);
    }

    public List<MessageResponseDto> getRoomHistory(Long roomId) {
        return messageRepository.findByRoomIdOrderBySentAtAsc(roomId)
                .stream()
                .map(this::toResponseDto)
                .toList();
    }

    private MessageResponseDto toResponseDto(Message message) {
        return new MessageResponseDto(
                message.getId(),
                message.getContent(),
                message.getSentAt(),
                message.getRoom().getId(),
                message.getSender().getUsername()
        );
    }
}

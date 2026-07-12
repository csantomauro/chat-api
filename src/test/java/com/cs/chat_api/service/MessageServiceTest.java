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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MessageServiceTest {

    @Mock
    private MessageRepository messageRepository;

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private MessageService messageService;

    private SendMessageRequestDto requestDto;
    private Room room;
    private User user;

    @BeforeEach
    void setUp() {
        requestDto = new SendMessageRequestDto();
        requestDto.setContent("Ciao a tutti!");
        requestDto.setRoomName("general");
        requestDto.setSenderUsername("mario");

        room = new Room();
        room.setId(1L);
        room.setName("general");

        user = new User();
        user.setId(1L);
        user.setUsername("mario");
    }

    @Test
    void sendMessage_shouldSucceed_whenRoomAndUserExist() {
        when(roomRepository.findByName("general")).thenReturn(Optional.of(room));
        when(userRepository.findByUsername("mario")).thenReturn(Optional.of(user));

        Message savedMessage = new Message();
        savedMessage.setId(1L);
        savedMessage.setContent("Ciao a tutti!");
        savedMessage.setSentAt(LocalDateTime.now());
        savedMessage.setRoom(room);
        savedMessage.setSender(user);
        when(messageRepository.save(any(Message.class))).thenReturn(savedMessage);

        MessageResponseDto result = messageService.sendMessage(requestDto);

        assertThat(result.getContent()).isEqualTo("Ciao a tutti!");
        assertThat(result.getSenderUsername()).isEqualTo("mario");
        assertThat(result.getRoomId()).isEqualTo(1L);
        verify(messageRepository).save(any(Message.class));
    }

    @Test
    void sendMessage_shouldThrow_whenRoomDoesNotExist() {
        when(roomRepository.findByName("general")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> messageService.sendMessage(requestDto))
                .isInstanceOf(RoomNotFoundException.class);

        verify(messageRepository, never()).save(any(Message.class));
    }

    @Test
    void sendMessage_shouldThrow_whenUserDoesNotExist() {
        when(roomRepository.findByName("general")).thenReturn(Optional.of(room));
        when(userRepository.findByUsername("mario")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> messageService.sendMessage(requestDto))
                .isInstanceOf(UserNotFoundException.class);

        verify(messageRepository, never()).save(any(Message.class));
    }
}

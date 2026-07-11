package com.cs.chat_api.service;

import com.cs.chat_api.dto.CreateRoomRequestDto;
import com.cs.chat_api.dto.RoomResponseDto;
import com.cs.chat_api.exception.RoomAlreadyExistsException;
import com.cs.chat_api.model.Room;
import com.cs.chat_api.repository.RoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RoomServiceTest {

    @Mock
    private RoomRepository roomRepository;

    @InjectMocks
    private RoomService roomService;

    private CreateRoomRequestDto requestDto;

    @BeforeEach
    void setup() {
        requestDto = new CreateRoomRequestDto();
        requestDto.setName("general");
    }

    @Test
    void createRoom_shouldSucceed_whenNameIsAvailable() {
        when(roomRepository.existsByName("general")).thenReturn(false);
        Room savedRoom = new Room();
        savedRoom.setId(1L);
        savedRoom.setName("general");
        savedRoom.setCreatedAt(LocalDateTime.now());
        when(roomRepository.save(any(Room.class))).thenReturn(savedRoom);

        RoomResponseDto result = roomService.createRoom(requestDto);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("general");
        verify(roomRepository).save(any(Room.class));
    }

    @Test
    void createRoom_shouldThrow_whenNameAlreadyExists() {
        when(roomRepository.existsByName("general")).thenReturn(true);

        assertThatThrownBy(() -> roomService.createRoom(requestDto))
                .isInstanceOf(RoomAlreadyExistsException.class)
                .hasMessageContaining("general");

        verify(roomRepository, never()).save(any(Room.class));
    }
}

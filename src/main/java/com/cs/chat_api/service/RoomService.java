package com.cs.chat_api.service;

import com.cs.chat_api.dto.CreateRoomRequestDto;
import com.cs.chat_api.dto.RoomResponseDto;
import com.cs.chat_api.exception.RoomAlreadyExistsException;
import com.cs.chat_api.model.Room;
import com.cs.chat_api.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomService {
    private final RoomRepository roomRepository;

    public RoomResponseDto createRoom(CreateRoomRequestDto request){
        if (roomRepository.existsByName(request.getName())) {
            throw new RoomAlreadyExistsException("Room already exists: " + request.getName());
        }

        Room room = new Room();
        room.setName(request.getName());

        Room saved =roomRepository.save(room);
        return toResponseDto(saved);
    }

    private RoomResponseDto toResponseDto(Room room) {
        return new RoomResponseDto(
                room.getId(),
                room.getName(),
                room.getCreatedAt(),
                List.of()
        );
    }
}

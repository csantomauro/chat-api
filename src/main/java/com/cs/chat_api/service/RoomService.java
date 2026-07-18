package com.cs.chat_api.service;

import com.cs.chat_api.dto.CreateRoomRequestDto;
import com.cs.chat_api.dto.RoomResponseDto;
import com.cs.chat_api.dto.UserResponseDto;
import com.cs.chat_api.exception.RoomAlreadyExistsException;
import com.cs.chat_api.exception.RoomNotFoundException;
import com.cs.chat_api.exception.UserNotFoundException;
import com.cs.chat_api.model.Room;
import com.cs.chat_api.model.User;
import com.cs.chat_api.repository.RoomRepository;
import com.cs.chat_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class RoomService {
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;

    public RoomResponseDto createRoom(CreateRoomRequestDto request){
        if (roomRepository.existsByName(request.getName())) {
            throw new RoomAlreadyExistsException("Room already exists: " + request.getName());
        }

        Room room = new Room();
        room.setName(request.getName());

        Room saved =roomRepository.save(room);
        return toResponseDto(saved);
    }

    public List<RoomResponseDto> getRooms() {
        return roomRepository.findAll().stream().map(this::toResponseDto).toList();
    }

    public RoomResponseDto getOrCreateDirectRoom(String usernameA, String usernameB) {
        User userA = userRepository.findByUsername(usernameA)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + usernameA));
        User userB = userRepository.findByUsername(usernameB)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + usernameB));

        String directRoomName = buildDirectRoomName(usernameA, usernameB);

        Room room = roomRepository.findByName(directRoomName)
                .orElseGet(() -> {
                    Room newRoom = new Room();
                    newRoom.setName(directRoomName);
                    newRoom.setDirect(true);
                    newRoom.getMembers().add(userA);
                    newRoom.getMembers().add(userB);
                    return roomRepository.save(newRoom);
                });

        return toResponseDto(room);
    }

    private String buildDirectRoomName(String usernameA, String usernameB) {
        List<String> sorted = Stream.of(usernameA, usernameB).sorted().toList();
        return "dm:" + sorted.get(0) + ":" + sorted.get(1);
    }

    public RoomResponseDto joinRoom(Long roomId, String username){
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RoomNotFoundException("Room not found with id: " + roomId));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + username));

        room.getMembers().add(user);
        Room updated = roomRepository.save(room);

        return toResponseDto(updated);
    }

    private RoomResponseDto toResponseDto(Room room) {
        List<UserResponseDto> memberDtos = room.getMembers().stream()
                .map(u -> new UserResponseDto(u.getId(), u.getUsername()))
                .toList();

        return new RoomResponseDto(
                room.getId(),
                room.getName(),
                room.getCreatedAt(),
                memberDtos
        );
    }
}

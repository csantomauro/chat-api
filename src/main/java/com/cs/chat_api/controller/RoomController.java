package com.cs.chat_api.controller;

import com.cs.chat_api.dto.CreateRoomRequestDto;
import com.cs.chat_api.dto.RoomResponseDto;
import com.cs.chat_api.service.RoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @PostMapping
    public ResponseEntity<RoomResponseDto> createRoom(@Valid @RequestBody CreateRoomRequestDto request) {
        RoomResponseDto created = roomService.createRoom(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public ResponseEntity<List<RoomResponseDto>> getRooms() {
        List<RoomResponseDto> rooms = roomService.getRooms();
        return ResponseEntity.ok(rooms);
    }

    @PostMapping("/{roomId}/join")
    public ResponseEntity<RoomResponseDto> joinRoom(
            @PathVariable Long roomId,
            @RequestParam String username
    ) {
        return ResponseEntity.ok(roomService.joinRoom(roomId, username));
    }

    @PostMapping("/direct")
    public ResponseEntity<RoomResponseDto> getOrCreateDirectRoom(
            @RequestParam String userA,
            @RequestParam String userB) {
        return ResponseEntity.ok(roomService.getOrCreateDirectRoom(userA, userB));
    }
}

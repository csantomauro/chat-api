package com.cs.chat_api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class RoomResponseDto {
    private Long id;
    private String name;
    private LocalDateTime createdAt;
    private List<UserResponseDto> members;
}

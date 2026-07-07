package com.cd.chat_api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class RoomResponseSto {
    private Long id;
    private String name;
    private LocalDateTime createdAt;
    private List<UserResponseDto> members;
}

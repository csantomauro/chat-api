package com.cd.chat_api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateRoomRequestDto {
    @NotBlank(message = "Room name is required")
    private String name;
}

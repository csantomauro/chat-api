package com.cs.chat_api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUserRequestDto {
    @NotBlank(message = "Username is required")
    private String username;
}

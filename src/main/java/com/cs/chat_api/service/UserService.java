package com.cs.chat_api.service;

import com.cs.chat_api.dto.CreateUserRequestDto;
import com.cs.chat_api.dto.UserResponseDto;
import com.cs.chat_api.exception.UserAlreadyExistsException;
import com.cs.chat_api.model.User;
import com.cs.chat_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserResponseDto createUser(CreateUserRequestDto request){
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new UserAlreadyExistsException("Username already taken: " + request.getUsername());
        }

        User user = new User();
        user.setUsername(request.getUsername());

        User saved = userRepository.save(user);
        return toResponseDto(saved);
    }

    private UserResponseDto toResponseDto(User user){
        return new UserResponseDto(user.getId(), user.getUsername());
    }
}

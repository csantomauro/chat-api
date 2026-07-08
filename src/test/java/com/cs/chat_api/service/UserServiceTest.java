package com.cs.chat_api.service;

import com.cs.chat_api.dto.CreateUserRequestDto;
import com.cs.chat_api.dto.UserResponseDto;
import com.cs.chat_api.exception.UserAlreadyExistsException;
import com.cs.chat_api.model.User;
import com.cs.chat_api.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private CreateUserRequestDto requestDto;

    @BeforeEach
    void setUp() {
        requestDto = new CreateUserRequestDto();
        requestDto.setUsername("mario");
    }

    @Test
    void createUser_shouldSucceed_whenUsernameIsAvailable() {
        // Arrange
        when(userRepository.existsByUsername("mario")).thenReturn(false);
        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setUsername("mario");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // Act
        UserResponseDto result = userService.createUser(requestDto);

        // Assert
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getUsername()).isEqualTo("mario");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void createUser_shouldThrow_whenUsernameAlreadyExists() {
        // Arrange
        when(userRepository.existsByUsername("mario")).thenReturn(true);

        // Act & Assert
        assertThatThrownBy(() -> userService.createUser(requestDto))
                .isInstanceOf(UserAlreadyExistsException.class)
                .hasMessageContaining("mario");

        verify(userRepository, never()).save(any(User.class));
    }
}

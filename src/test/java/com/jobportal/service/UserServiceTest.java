package com.jobportal.service;

import com.jobportal.dto.UserRequestDTO;
import com.jobportal.dto.UserResponseDTO;
import com.jobportal.entity.Role;
import com.jobportal.entity.User;
import com.jobportal.exception.BadRequestException;
import com.jobportal.exception.ResourceNotFoundException;
import com.jobportal.repository.UserRepository;
import com.jobportal.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private UserRequestDTO userRequestDTO;

    @BeforeEach
    void setUp() {
        user = new User(1L, "Alice Johnson", "alice@example.com", "password123", Role.JOB_SEEKER);
        userRequestDTO = new UserRequestDTO("Alice Johnson", "alice@example.com", "password123", Role.JOB_SEEKER);
    }

    @Test
    @DisplayName("Should successfully register a new user")
    void testRegisterUser_Success() {
        when(userRepository.existsByEmail(userRequestDTO.getEmail())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserResponseDTO response = userService.registerUser(userRequestDTO);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getName()).isEqualTo("Alice Johnson");
        assertThat(response.getEmail()).isEqualTo("alice@example.com");
        assertThat(response.getRole()).isEqualTo(Role.JOB_SEEKER);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Should throw BadRequestException when registering with duplicate email")
    void testRegisterUser_DuplicateEmail() {
        when(userRepository.existsByEmail(userRequestDTO.getEmail())).thenReturn(true);

        assertThatThrownBy(() -> userService.registerUser(userRequestDTO))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Email is already registered");

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Should retrieve user by ID successfully")
    void testGetUserById_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserResponseDTO response = userService.getUserById(1L);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getName()).isEqualTo("Alice Johnson");
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when user ID is not found")
    void testGetUserById_NotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getUserById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("User not found with id: 99");
    }

    @Test
    @DisplayName("Should retrieve all users")
    void testGetAllUsers() {
        User user2 = new User(2L, "Bob Employer", "bob@example.com", "password123", Role.EMPLOYER);
        when(userRepository.findAll()).thenReturn(Arrays.asList(user, user2));

        List<UserResponseDTO> users = userService.getAllUsers();

        assertThat(users).hasSize(2);
        assertThat(users.get(0).getName()).isEqualTo("Alice Johnson");
        assertThat(users.get(1).getName()).isEqualTo("Bob Employer");
    }

    @Test
    @DisplayName("Should successfully update user")
    void testUpdateUser_Success() {
        UserRequestDTO updateRequest = new UserRequestDTO("Alice Updated", "alice@example.com", "newpass123", Role.JOB_SEEKER);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserResponseDTO updated = userService.updateUser(1L, updateRequest);

        assertThat(updated).isNotNull();
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Should delete user successfully")
    void testDeleteUser_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        doNothing().when(userRepository).delete(user);

        userService.deleteUser(1L);

        verify(userRepository, times(1)).delete(user);
    }
}

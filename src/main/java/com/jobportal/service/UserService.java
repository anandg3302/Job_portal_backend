package com.jobportal.service;

import com.jobportal.dto.UserRequestDTO;
import com.jobportal.dto.UserResponseDTO;

import java.util.List;

/**
 * Service interface for User business logic.
 */
public interface UserService {

    UserResponseDTO registerUser(UserRequestDTO request);

    List<UserResponseDTO> getAllUsers();

    UserResponseDTO getUserById(Long id);

    UserResponseDTO updateUser(Long id, UserRequestDTO request);

    void deleteUser(Long id);
}

package com.jobportal.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jobportal.dto.UserRequestDTO;
import com.jobportal.dto.UserResponseDTO;
import com.jobportal.entity.Role;
import com.jobportal.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @Test
    @DisplayName("POST /api/users - Should register user and return 201 Created")
    void testRegisterUser() throws Exception {
        UserRequestDTO request = new UserRequestDTO("John Doe", "john@example.com", "secret123", Role.JOB_SEEKER);
        UserResponseDTO response = new UserResponseDTO(1L, "John Doe", "john@example.com", Role.JOB_SEEKER);

        when(userService.registerUser(any(UserRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.name").value("John Doe"))
                .andExpect(jsonPath("$.data.email").value("john@example.com"))
                .andExpect(jsonPath("$.data.role").value("JOB_SEEKER"));
    }

    @Test
    @DisplayName("GET /api/users - Should return list of users with 200 OK")
    void testGetAllUsers() throws Exception {
        UserResponseDTO response = new UserResponseDTO(1L, "John Doe", "john@example.com", Role.JOB_SEEKER);
        when(userService.getAllUsers()).thenReturn(Collections.singletonList(response));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[0].name").value("John Doe"));
    }

    @Test
    @DisplayName("GET /api/users/{id} - Should return user by ID with 200 OK")
    void testGetUserById() throws Exception {
        UserResponseDTO response = new UserResponseDTO(1L, "John Doe", "john@example.com", Role.JOB_SEEKER);
        when(userService.getUserById(1L)).thenReturn(response);

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(1));
    }

    @Test
    @DisplayName("DELETE /api/users/{id} - Should delete user and return 200 OK")
    void testDeleteUser() throws Exception {
        doNothing().when(userService).deleteUser(1L);

        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("User deleted successfully"));
    }
}

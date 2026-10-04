package com.jobportal.controller;

import com.jobportal.dto.ApiResponse;
import com.jobportal.dto.UserRequestDTO;
import com.jobportal.dto.UserResponseDTO;
import com.jobportal.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST Controller for User management operations.
 */
@RestController
@RequestMapping("/api/users")
@Tag(name = "User Management", description = "Endpoints for registering, retrieving, updating, and deleting users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @Operation(summary = "Register a new user", description = "Creates a new user profile as JOB_SEEKER or EMPLOYER")
    public ResponseEntity<ApiResponse<UserResponseDTO>> registerUser(@Valid @RequestBody UserRequestDTO request) {
        UserResponseDTO userResponse = userService.registerUser(request);
        return new ResponseEntity<>(
                ApiResponse.success("User registered successfully", userResponse),
                HttpStatus.CREATED
        );
    }

    @GetMapping
    @Operation(summary = "Get all users", description = "Retrieves a list of all registered users")
    public ResponseEntity<ApiResponse<List<UserResponseDTO>>> getAllUsers() {
        List<UserResponseDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(ApiResponse.success("Users retrieved successfully", users));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID", description = "Retrieves user details by their unique identifier")
    public ResponseEntity<ApiResponse<UserResponseDTO>> getUserById(@PathVariable Long id) {
        UserResponseDTO user = userService.getUserById(id);
        return ResponseEntity.ok(ApiResponse.success("User retrieved successfully", user));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update user", description = "Updates an existing user's information")
    public ResponseEntity<ApiResponse<UserResponseDTO>> updateUser(@PathVariable Long id,
                                                                   @Valid @RequestBody UserRequestDTO request) {
        UserResponseDTO updatedUser = userService.updateUser(id, request);
        return ResponseEntity.ok(ApiResponse.success("User updated successfully", updatedUser));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user", description = "Deletes a user account by their unique identifier")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(ApiResponse.success("User deleted successfully"));
    }
}

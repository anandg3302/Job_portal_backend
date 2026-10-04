package com.jobportal.dto;

import com.jobportal.entity.Role;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Data Transfer Object for User responses (excludes sensitive fields like password).
 */
@Schema(description = "User Response Details")
public class UserResponseDTO {

    @Schema(description = "Unique identifier of the user", example = "1")
    private Long id;

    @Schema(description = "Name of the user", example = "John Doe")
    private String name;

    @Schema(description = "Email address", example = "john.doe@example.com")
    private String email;

    @Schema(description = "Role of the user", example = "JOB_SEEKER")
    private Role role;

    public UserResponseDTO() {
    }

    public UserResponseDTO(Long id, String name, String email, Role role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}

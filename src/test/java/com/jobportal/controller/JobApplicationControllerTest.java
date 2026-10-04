package com.jobportal.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jobportal.dto.JobApplicationRequestDTO;
import com.jobportal.dto.JobApplicationResponseDTO;
import com.jobportal.dto.JobApplicationStatusUpdateDTO;
import com.jobportal.entity.ApplicationStatus;
import com.jobportal.service.JobApplicationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(JobApplicationController.class)
class JobApplicationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JobApplicationService applicationService;

    @Test
    @DisplayName("POST /api/applications - Should submit job application and return 201 Created")
    void testApplyForJob() throws Exception {
        JobApplicationRequestDTO request = new JobApplicationRequestDTO(10L, 2L);
        JobApplicationResponseDTO response = new JobApplicationResponseDTO(
                100L, 10L, "Java Developer", "TechCorp", 2L, "Jane Seeker", "jane@seeker.com",
                LocalDateTime.now(), ApplicationStatus.APPLIED
        );

        when(applicationService.applyForJob(any(JobApplicationRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/api/applications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(100))
                .andExpect(jsonPath("$.data.jobTitle").value("Java Developer"))
                .andExpect(jsonPath("$.data.status").value("APPLIED"));
    }

    @Test
    @DisplayName("GET /api/applications/user/{userId} - Should return applications for specific user")
    void testGetApplicationsByUser() throws Exception {
        JobApplicationResponseDTO response = new JobApplicationResponseDTO(
                100L, 10L, "Java Developer", "TechCorp", 2L, "Jane Seeker", "jane@seeker.com",
                LocalDateTime.now(), ApplicationStatus.APPLIED
        );

        when(applicationService.getApplicationsByUserId(2L)).thenReturn(Collections.singletonList(response));

        mockMvc.perform(get("/api/applications/user/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].id").value(100))
                .andExpect(jsonPath("$.data[0].userName").value("Jane Seeker"));
    }

    @Test
    @DisplayName("PUT /api/applications/{id}/status - Should update application status")
    void testUpdateStatus() throws Exception {
        JobApplicationStatusUpdateDTO updateDTO = new JobApplicationStatusUpdateDTO(ApplicationStatus.SHORTLISTED);
        JobApplicationResponseDTO response = new JobApplicationResponseDTO(
                100L, 10L, "Java Developer", "TechCorp", 2L, "Jane Seeker", "jane@seeker.com",
                LocalDateTime.now(), ApplicationStatus.SHORTLISTED
        );

        when(applicationService.updateApplicationStatus(eq(100L), eq(ApplicationStatus.SHORTLISTED))).thenReturn(response);

        mockMvc.perform(put("/api/applications/100/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.status").value("SHORTLISTED"));
    }
}

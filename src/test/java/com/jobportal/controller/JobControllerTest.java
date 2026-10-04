package com.jobportal.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jobportal.dto.JobRequestDTO;
import com.jobportal.dto.JobResponseDTO;
import com.jobportal.service.JobService;
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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(JobController.class)
class JobControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JobService jobService;

    @Test
    @DisplayName("POST /api/jobs - Should create a job and return 201 Created")
    void testCreateJob() throws Exception {
        JobRequestDTO request = new JobRequestDTO("Java Developer", "Description of job", "Google", "Mountain View", 120000.0, 1L);
        JobResponseDTO response = new JobResponseDTO(10L, "Java Developer", "Description of job", "Google", "Mountain View", 120000.0, 1L, "Employer HR");

        when(jobService.createJob(any(JobRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/api/jobs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(10))
                .andExpect(jsonPath("$.data.title").value("Java Developer"))
                .andExpect(jsonPath("$.data.company").value("Google"));
    }

    @Test
    @DisplayName("GET /api/jobs - Should return all jobs with 200 OK")
    void testGetAllJobs() throws Exception {
        JobResponseDTO response = new JobResponseDTO(10L, "Java Developer", "Description of job", "Google", "Mountain View", 120000.0, 1L, "Employer HR");
        when(jobService.getAllJobs()).thenReturn(Collections.singletonList(response));

        mockMvc.perform(get("/api/jobs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].id").value(10))
                .andExpect(jsonPath("$.data[0].title").value("Java Developer"));
    }

    @Test
    @DisplayName("GET /api/jobs/search - Should search jobs by title or location")
    void testSearchJobs() throws Exception {
        JobResponseDTO response = new JobResponseDTO(10L, "Java Developer", "Description of job", "Google", "Mountain View", 120000.0, 1L, "Employer HR");
        when(jobService.searchJobs("Java", "Mountain View")).thenReturn(Collections.singletonList(response));

        mockMvc.perform(get("/api/jobs/search")
                        .param("title", "Java")
                        .param("location", "Mountain View"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].title").value("Java Developer"));
    }
}

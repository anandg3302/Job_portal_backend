package com.jobportal.service;

import com.jobportal.dto.JobRequestDTO;
import com.jobportal.dto.JobResponseDTO;
import com.jobportal.entity.Job;
import com.jobportal.entity.Role;
import com.jobportal.entity.User;
import com.jobportal.exception.BadRequestException;
import com.jobportal.exception.ResourceNotFoundException;
import com.jobportal.repository.JobRepository;
import com.jobportal.repository.UserRepository;
import com.jobportal.service.impl.JobServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JobServiceTest {

    @Mock
    private JobRepository jobRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private JobServiceImpl jobService;

    private User employer;
    private User jobSeeker;
    private Job job;
    private JobRequestDTO jobRequestDTO;

    @BeforeEach
    void setUp() {
        employer = new User(1L, "TechCorp HR", "hr@techcorp.com", "password123", Role.EMPLOYER);
        jobSeeker = new User(2L, "Jane JobSeeker", "jane@example.com", "password123", Role.JOB_SEEKER);

        job = new Job(10L, "Java Developer", "Spring Boot development", "TechCorp", "Remote", 85000.0, employer);
        jobRequestDTO = new JobRequestDTO("Java Developer", "Spring Boot development", "TechCorp", "Remote", 85000.0, 1L);
    }

    @Test
    @DisplayName("Should successfully create a job when posted by an EMPLOYER")
    void testCreateJob_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(employer));
        when(jobRepository.save(any(Job.class))).thenReturn(job);

        JobResponseDTO response = jobService.createJob(jobRequestDTO);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(10L);
        assertThat(response.getTitle()).isEqualTo("Java Developer");
        assertThat(response.getCompany()).isEqualTo("TechCorp");
        assertThat(response.getEmployerId()).isEqualTo(1L);
        verify(jobRepository, times(1)).save(any(Job.class));
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when employer is not found")
    void testCreateJob_EmployerNotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());
        jobRequestDTO.setEmployerId(99L);

        assertThatThrownBy(() -> jobService.createJob(jobRequestDTO))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Employer not found with id: 99");

        verify(jobRepository, never()).save(any(Job.class));
    }

    @Test
    @DisplayName("Should throw BadRequestException when user posting job is not an EMPLOYER")
    void testCreateJob_UserNotEmployer() {
        when(userRepository.findById(2L)).thenReturn(Optional.of(jobSeeker));
        jobRequestDTO.setEmployerId(2L);

        assertThatThrownBy(() -> jobService.createJob(jobRequestDTO))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Only employers can post jobs");

        verify(jobRepository, never()).save(any(Job.class));
    }

    @Test
    @DisplayName("Should retrieve job by ID")
    void testGetJobById_Success() {
        when(jobRepository.findById(10L)).thenReturn(Optional.of(job));

        JobResponseDTO response = jobService.getJobById(10L);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(10L);
        assertThat(response.getTitle()).isEqualTo("Java Developer");
    }

    @Test
    @DisplayName("Should search jobs by title and location")
    void testSearchJobs() {
        when(jobRepository.searchJobs("Java", "Remote")).thenReturn(Collections.singletonList(job));

        List<JobResponseDTO> results = jobService.searchJobs("Java", "Remote");

        assertThat(results).hasSize(1);
        assertThat(results.get(0).getTitle()).isEqualTo("Java Developer");
        verify(jobRepository, times(1)).searchJobs("Java", "Remote");
    }

    @Test
    @DisplayName("Should delete job by ID")
    void testDeleteJob() {
        when(jobRepository.findById(10L)).thenReturn(Optional.of(job));
        doNothing().when(jobRepository).delete(job);

        jobService.deleteJob(10L);

        verify(jobRepository, times(1)).delete(job);
    }
}

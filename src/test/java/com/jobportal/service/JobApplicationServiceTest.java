package com.jobportal.service;

import com.jobportal.dto.JobApplicationRequestDTO;
import com.jobportal.dto.JobApplicationResponseDTO;
import com.jobportal.entity.ApplicationStatus;
import com.jobportal.entity.Job;
import com.jobportal.entity.JobApplication;
import com.jobportal.entity.Role;
import com.jobportal.entity.User;
import com.jobportal.exception.BadRequestException;
import com.jobportal.exception.ResourceNotFoundException;
import com.jobportal.repository.JobApplicationRepository;
import com.jobportal.repository.JobRepository;
import com.jobportal.repository.UserRepository;
import com.jobportal.service.impl.JobApplicationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JobApplicationServiceTest {

    @Mock
    private JobApplicationRepository jobApplicationRepository;

    @Mock
    private JobRepository jobRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private JobApplicationServiceImpl applicationService;

    private User employer;
    private User jobSeeker;
    private Job job;
    private JobApplication application;
    private JobApplicationRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        employer = new User(1L, "Employer Corp", "emp@corp.com", "pass123", Role.EMPLOYER);
        jobSeeker = new User(2L, "Job Seeker Bob", "bob@seeker.com", "pass123", Role.JOB_SEEKER);

        job = new Job(10L, "Backend Engineer", "Build REST APIs", "Employer Corp", "Remote", 90000.0, employer);
        application = new JobApplication(100L, job, jobSeeker, LocalDateTime.now(), ApplicationStatus.APPLIED);
        requestDTO = new JobApplicationRequestDTO(10L, 2L);
    }

    @Test
    @DisplayName("Should successfully apply for a job when user is JOB_SEEKER")
    void testApplyForJob_Success() {
        when(jobRepository.findById(10L)).thenReturn(Optional.of(job));
        when(userRepository.findById(2L)).thenReturn(Optional.of(jobSeeker));
        when(jobApplicationRepository.existsByJobIdAndUserId(10L, 2L)).thenReturn(false);
        when(jobApplicationRepository.save(any(JobApplication.class))).thenReturn(application);

        JobApplicationResponseDTO response = applicationService.applyForJob(requestDTO);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(100L);
        assertThat(response.getJobId()).isEqualTo(10L);
        assertThat(response.getUserId()).isEqualTo(2L);
        assertThat(response.getStatus()).isEqualTo(ApplicationStatus.APPLIED);
        verify(jobApplicationRepository, times(1)).save(any(JobApplication.class));
    }

    @Test
    @DisplayName("Should throw BadRequestException if applicant is not a JOB_SEEKER")
    void testApplyForJob_ApplicantNotJobSeeker() {
        User anotherEmployer = new User(3L, "Other Employer", "other@corp.com", "pass", Role.EMPLOYER);
        when(jobRepository.findById(10L)).thenReturn(Optional.of(job));
        when(userRepository.findById(3L)).thenReturn(Optional.of(anotherEmployer));
        requestDTO.setUserId(3L);

        assertThatThrownBy(() -> applicationService.applyForJob(requestDTO))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Only job seekers can apply");

        verify(jobApplicationRepository, never()).save(any(JobApplication.class));
    }

    @Test
    @DisplayName("Should throw BadRequestException when user already applied for job")
    void testApplyForJob_AlreadyApplied() {
        when(jobRepository.findById(10L)).thenReturn(Optional.of(job));
        when(userRepository.findById(2L)).thenReturn(Optional.of(jobSeeker));
        when(jobApplicationRepository.existsByJobIdAndUserId(10L, 2L)).thenReturn(true);

        assertThatThrownBy(() -> applicationService.applyForJob(requestDTO))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("User has already applied for this job");

        verify(jobApplicationRepository, never()).save(any(JobApplication.class));
    }

    @Test
    @DisplayName("Should retrieve applications by user ID")
    void testGetApplicationsByUserId() {
        when(userRepository.existsById(2L)).thenReturn(true);
        when(jobApplicationRepository.findByUserId(2L)).thenReturn(Collections.singletonList(application));

        List<JobApplicationResponseDTO> results = applicationService.getApplicationsByUserId(2L);

        assertThat(results).hasSize(1);
        assertThat(results.get(0).getUserEmail()).isEqualTo("bob@seeker.com");
    }

    @Test
    @DisplayName("Should retrieve applications for a specific job")
    void testGetApplicationsByJobId() {
        when(jobRepository.existsById(10L)).thenReturn(true);
        when(jobApplicationRepository.findByJobId(10L)).thenReturn(Collections.singletonList(application));

        List<JobApplicationResponseDTO> results = applicationService.getApplicationsByJobId(10L);

        assertThat(results).hasSize(1);
        assertThat(results.get(0).getJobTitle()).isEqualTo("Backend Engineer");
    }

    @Test
    @DisplayName("Should successfully update application status")
    void testUpdateApplicationStatus() {
        when(jobApplicationRepository.findById(100L)).thenReturn(Optional.of(application));
        when(jobApplicationRepository.save(any(JobApplication.class))).thenReturn(application);

        JobApplicationResponseDTO updated = applicationService.updateApplicationStatus(100L, ApplicationStatus.SHORTLISTED);

        assertThat(updated).isNotNull();
        verify(jobApplicationRepository, times(1)).save(application);
    }
}

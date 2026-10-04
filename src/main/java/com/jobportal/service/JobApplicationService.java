package com.jobportal.service;

import com.jobportal.dto.JobApplicationRequestDTO;
import com.jobportal.dto.JobApplicationResponseDTO;
import com.jobportal.entity.ApplicationStatus;

import java.util.List;

/**
 * Service interface for Job Application business logic.
 */
public interface JobApplicationService {

    JobApplicationResponseDTO applyForJob(JobApplicationRequestDTO request);

    List<JobApplicationResponseDTO> getAllApplications();

    List<JobApplicationResponseDTO> getApplicationsByUserId(Long userId);

    List<JobApplicationResponseDTO> getApplicationsByJobId(Long jobId);

    JobApplicationResponseDTO updateApplicationStatus(Long id, ApplicationStatus status);
}

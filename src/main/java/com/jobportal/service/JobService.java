package com.jobportal.service;

import com.jobportal.dto.JobRequestDTO;
import com.jobportal.dto.JobResponseDTO;

import java.util.List;

/**
 * Service interface for Job business logic.
 */
public interface JobService {

    JobResponseDTO createJob(JobRequestDTO request);

    List<JobResponseDTO> getAllJobs();

    JobResponseDTO getJobById(Long id);

    JobResponseDTO updateJob(Long id, JobRequestDTO request);

    void deleteJob(Long id);

    List<JobResponseDTO> searchJobs(String title, String location);
}

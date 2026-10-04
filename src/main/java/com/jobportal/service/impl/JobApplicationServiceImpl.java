package com.jobportal.service.impl;

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
import com.jobportal.service.JobApplicationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service implementation for Job Application operations.
 */
@Service
@Transactional
public class JobApplicationServiceImpl implements JobApplicationService {

    private final JobApplicationRepository jobApplicationRepository;
    private final JobRepository jobRepository;
    private final UserRepository userRepository;

    public JobApplicationServiceImpl(JobApplicationRepository jobApplicationRepository,
                                     JobRepository jobRepository,
                                     UserRepository userRepository) {
        this.jobApplicationRepository = jobApplicationRepository;
        this.jobRepository = jobRepository;
        this.userRepository = userRepository;
    }

    @Override
    public JobApplicationResponseDTO applyForJob(JobApplicationRequestDTO request) {
        Job job = jobRepository.findById(request.getJobId())
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with id: " + request.getJobId()));

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + request.getUserId()));

        if (user.getRole() != Role.JOB_SEEKER) {
            throw new BadRequestException("User with id " + request.getUserId() + " is not a JOB_SEEKER. Only job seekers can apply.");
        }

        if (jobApplicationRepository.existsByJobIdAndUserId(request.getJobId(), request.getUserId())) {
            throw new BadRequestException("User has already applied for this job.");
        }

        JobApplication application = new JobApplication();
        application.setJob(job);
        application.setUser(user);
        application.setApplicationDate(LocalDateTime.now());
        application.setStatus(ApplicationStatus.APPLIED);

        JobApplication savedApplication = jobApplicationRepository.save(application);
        return mapToResponseDTO(savedApplication);
    }

    @Override
    @Transactional(readOnly = true)
    public List<JobApplicationResponseDTO> getAllApplications() {
        return jobApplicationRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<JobApplicationResponseDTO> getApplicationsByUserId(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }

        return jobApplicationRepository.findByUserId(userId).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<JobApplicationResponseDTO> getApplicationsByJobId(Long jobId) {
        if (!jobRepository.existsById(jobId)) {
            throw new ResourceNotFoundException("Job not found with id: " + jobId);
        }

        return jobApplicationRepository.findByJobId(jobId).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public JobApplicationResponseDTO updateApplicationStatus(Long id, ApplicationStatus status) {
        if (status == null) {
            throw new BadRequestException("Application status must not be null.");
        }

        JobApplication application = jobApplicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job application not found with id: " + id));

        application.setStatus(status);
        JobApplication updatedApplication = jobApplicationRepository.save(application);
        return mapToResponseDTO(updatedApplication);
    }

    private JobApplicationResponseDTO mapToResponseDTO(JobApplication application) {
        return new JobApplicationResponseDTO(
                application.getId(),
                application.getJob() != null ? application.getJob().getId() : null,
                application.getJob() != null ? application.getJob().getTitle() : null,
                application.getJob() != null ? application.getJob().getCompany() : null,
                application.getUser() != null ? application.getUser().getId() : null,
                application.getUser() != null ? application.getUser().getName() : null,
                application.getUser() != null ? application.getUser().getEmail() : null,
                application.getApplicationDate(),
                application.getStatus()
        );
    }
}

package com.jobportal.service.impl;

import com.jobportal.dto.JobRequestDTO;
import com.jobportal.dto.JobResponseDTO;
import com.jobportal.entity.Job;
import com.jobportal.entity.Role;
import com.jobportal.entity.User;
import com.jobportal.exception.BadRequestException;
import com.jobportal.exception.ResourceNotFoundException;
import com.jobportal.repository.JobRepository;
import com.jobportal.repository.UserRepository;
import com.jobportal.service.JobService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service implementation for Job operations.
 */
@Service
@Transactional
public class JobServiceImpl implements JobService {

    private final JobRepository jobRepository;
    private final UserRepository userRepository;

    public JobServiceImpl(JobRepository jobRepository, UserRepository userRepository) {
        this.jobRepository = jobRepository;
        this.userRepository = userRepository;
    }

    @Override
    public JobResponseDTO createJob(JobRequestDTO request) {
        User employer = userRepository.findById(request.getEmployerId())
                .orElseThrow(() -> new ResourceNotFoundException("Employer not found with id: " + request.getEmployerId()));

        if (employer.getRole() != Role.EMPLOYER) {
            throw new BadRequestException("User with id " + request.getEmployerId() + " is not an EMPLOYER. Only employers can post jobs.");
        }

        Job job = new Job();
        job.setTitle(request.getTitle());
        job.setDescription(request.getDescription());
        job.setCompany(request.getCompany());
        job.setLocation(request.getLocation());
        job.setSalary(request.getSalary());
        job.setEmployer(employer);

        Job savedJob = jobRepository.save(job);
        return mapToResponseDTO(savedJob);
    }

    @Override
    @Transactional(readOnly = true)
    public List<JobResponseDTO> getAllJobs() {
        return jobRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public JobResponseDTO getJobById(Long id) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with id: " + id));
        return mapToResponseDTO(job);
    }

    @Override
    public JobResponseDTO updateJob(Long id, JobRequestDTO request) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with id: " + id));

        User employer = userRepository.findById(request.getEmployerId())
                .orElseThrow(() -> new ResourceNotFoundException("Employer not found with id: " + request.getEmployerId()));

        if (employer.getRole() != Role.EMPLOYER) {
            throw new BadRequestException("User with id " + request.getEmployerId() + " is not an EMPLOYER.");
        }

        job.setTitle(request.getTitle());
        job.setDescription(request.getDescription());
        job.setCompany(request.getCompany());
        job.setLocation(request.getLocation());
        job.setSalary(request.getSalary());
        job.setEmployer(employer);

        Job updatedJob = jobRepository.save(job);
        return mapToResponseDTO(updatedJob);
    }

    @Override
    public void deleteJob(Long id) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with id: " + id));
        jobRepository.delete(job);
    }

    @Override
    @Transactional(readOnly = true)
    public List<JobResponseDTO> searchJobs(String title, String location) {
        String cleanTitle = (title != null && !title.trim().isEmpty()) ? title.trim() : null;
        String cleanLocation = (location != null && !location.trim().isEmpty()) ? location.trim() : null;

        if (cleanTitle == null && cleanLocation == null) {
            return getAllJobs();
        }

        return jobRepository.searchJobs(cleanTitle, cleanLocation).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    private JobResponseDTO mapToResponseDTO(Job job) {
        return new JobResponseDTO(
                job.getId(),
                job.getTitle(),
                job.getDescription(),
                job.getCompany(),
                job.getLocation(),
                job.getSalary(),
                job.getEmployer() != null ? job.getEmployer().getId() : null,
                job.getEmployer() != null ? job.getEmployer().getName() : null
        );
    }
}

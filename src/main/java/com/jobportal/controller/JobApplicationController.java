package com.jobportal.controller;

import com.jobportal.dto.ApiResponse;
import com.jobportal.dto.JobApplicationRequestDTO;
import com.jobportal.dto.JobApplicationResponseDTO;
import com.jobportal.dto.JobApplicationStatusUpdateDTO;
import com.jobportal.service.JobApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST Controller for Job Application operations.
 */
@RestController
@RequestMapping("/api/applications")
@Tag(name = "Job Applications", description = "Endpoints for applying to jobs, tracking applications, and updating application status")
public class JobApplicationController {

    private final JobApplicationService applicationService;

    public JobApplicationController(JobApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @PostMapping
    @Operation(summary = "Apply for a job", description = "Allows a job seeker to apply for a job posting")
    public ResponseEntity<ApiResponse<JobApplicationResponseDTO>> applyForJob(
            @Valid @RequestBody JobApplicationRequestDTO request) {
        JobApplicationResponseDTO response = applicationService.applyForJob(request);
        return new ResponseEntity<>(
                ApiResponse.success("Application submitted successfully", response),
                HttpStatus.CREATED
        );
    }

    @GetMapping
    @Operation(summary = "Get all applications", description = "Retrieves all submitted job applications across the platform")
    public ResponseEntity<ApiResponse<List<JobApplicationResponseDTO>>> getAllApplications() {
        List<JobApplicationResponseDTO> applications = applicationService.getAllApplications();
        return ResponseEntity.ok(ApiResponse.success("Applications retrieved successfully", applications));
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get applications by user ID", description = "Retrieves all applications submitted by a specific job seeker")
    public ResponseEntity<ApiResponse<List<JobApplicationResponseDTO>>> getApplicationsByUser(@PathVariable Long userId) {
        List<JobApplicationResponseDTO> applications = applicationService.getApplicationsByUserId(userId);
        return ResponseEntity.ok(ApiResponse.success("User applications retrieved successfully", applications));
    }

    @GetMapping("/job/{jobId}")
    @Operation(summary = "Get applications for a particular job", description = "Retrieves all applications submitted for a specific job listing")
    public ResponseEntity<ApiResponse<List<JobApplicationResponseDTO>>> getApplicationsByJob(@PathVariable Long jobId) {
        List<JobApplicationResponseDTO> applications = applicationService.getApplicationsByJobId(jobId);
        return ResponseEntity.ok(ApiResponse.success("Job applications retrieved successfully", applications));
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "Update application status", description = "Updates status of an application (APPLIED, SHORTLISTED, REJECTED, SELECTED)")
    public ResponseEntity<ApiResponse<JobApplicationResponseDTO>> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody JobApplicationStatusUpdateDTO statusUpdateDTO) {
        JobApplicationResponseDTO updatedApplication = applicationService.updateApplicationStatus(id, statusUpdateDTO.getStatus());
        return ResponseEntity.ok(ApiResponse.success("Application status updated successfully", updatedApplication));
    }
}

package com.jobportal.controller;

import com.jobportal.dto.ApiResponse;
import com.jobportal.dto.JobRequestDTO;
import com.jobportal.dto.JobResponseDTO;
import com.jobportal.service.JobService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST Controller for Job management operations.
 */
@RestController
@RequestMapping("/api/jobs")
@Tag(name = "Job Management", description = "Endpoints for posting, retrieving, searching, updating, and deleting jobs")
public class JobController {

    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @PostMapping
    @Operation(summary = "Create a job", description = "Allows an employer to post a new job vacancy")
    public ResponseEntity<ApiResponse<JobResponseDTO>> createJob(@Valid @RequestBody JobRequestDTO request) {
        JobResponseDTO jobResponse = jobService.createJob(request);
        return new ResponseEntity<>(
                ApiResponse.success("Job created successfully", jobResponse),
                HttpStatus.CREATED
        );
    }

    @GetMapping
    @Operation(summary = "Get all jobs", description = "Retrieves all available job postings")
    public ResponseEntity<ApiResponse<List<JobResponseDTO>>> getAllJobs() {
        List<JobResponseDTO> jobs = jobService.getAllJobs();
        return ResponseEntity.ok(ApiResponse.success("Jobs retrieved successfully", jobs));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get job by ID", description = "Retrieves specific job details by its ID")
    public ResponseEntity<ApiResponse<JobResponseDTO>> getJobById(@PathVariable Long id) {
        JobResponseDTO job = jobService.getJobById(id);
        return ResponseEntity.ok(ApiResponse.success("Job retrieved successfully", job));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a job", description = "Updates job details for a given job ID")
    public ResponseEntity<ApiResponse<JobResponseDTO>> updateJob(@PathVariable Long id,
                                                                 @Valid @RequestBody JobRequestDTO request) {
        JobResponseDTO updatedJob = jobService.updateJob(id, request);
        return ResponseEntity.ok(ApiResponse.success("Job updated successfully", updatedJob));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a job", description = "Deletes a job listing by its ID")
    public ResponseEntity<ApiResponse<Void>> deleteJob(@PathVariable Long id) {
        jobService.deleteJob(id);
        return ResponseEntity.ok(ApiResponse.success("Job deleted successfully"));
    }

    @GetMapping("/search")
    @Operation(summary = "Search jobs", description = "Searches for jobs by title and/or location (case-insensitive)")
    public ResponseEntity<ApiResponse<List<JobResponseDTO>>> searchJobs(
            @Parameter(description = "Job title search keyword") @RequestParam(required = false) String title,
            @Parameter(description = "Job location search keyword") @RequestParam(required = false) String location) {
        List<JobResponseDTO> jobs = jobService.searchJobs(title, location);
        return ResponseEntity.ok(ApiResponse.success("Search completed successfully", jobs));
    }
}

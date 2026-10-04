package com.jobportal.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Data Transfer Object for submitting a Job Application.
 */
@Schema(description = "Job Application Submission Request")
public class JobApplicationRequestDTO {

    @NotNull(message = "Job ID is required")
    @Schema(description = "ID of the job being applied for", example = "10")
    private Long jobId;

    @NotNull(message = "User ID (Job Seeker) is required")
    @Schema(description = "ID of the applicant (must be a JOB_SEEKER)", example = "2")
    private Long userId;

    public JobApplicationRequestDTO() {
    }

    public JobApplicationRequestDTO(Long jobId, Long userId) {
        this.jobId = jobId;
        this.userId = userId;
    }

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}

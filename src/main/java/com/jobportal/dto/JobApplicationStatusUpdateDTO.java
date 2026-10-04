package com.jobportal.dto;

import com.jobportal.entity.ApplicationStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Data Transfer Object for updating application status.
 */
@Schema(description = "Job Application Status Update Request")
public class JobApplicationStatusUpdateDTO {

    @NotNull(message = "Status is required (APPLIED, SHORTLISTED, REJECTED, SELECTED)")
    @Schema(description = "New application status", example = "SHORTLISTED")
    private ApplicationStatus status;

    public JobApplicationStatusUpdateDTO() {
    }

    public JobApplicationStatusUpdateDTO(ApplicationStatus status) {
        this.status = status;
    }

    public ApplicationStatus getStatus() {
        return status;
    }

    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }
}

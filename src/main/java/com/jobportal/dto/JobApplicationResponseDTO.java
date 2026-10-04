package com.jobportal.dto;

import com.jobportal.entity.ApplicationStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for Job Application responses.
 */
@Schema(description = "Job Application Response Details")
public class JobApplicationResponseDTO {

    @Schema(description = "Application ID", example = "100")
    private Long id;

    @Schema(description = "Job ID", example = "10")
    private Long jobId;

    @Schema(description = "Job title", example = "Senior Java Developer")
    private String jobTitle;

    @Schema(description = "Hiring company name", example = "TechCorp Solutions")
    private String company;

    @Schema(description = "Applicant User ID", example = "2")
    private Long userId;

    @Schema(description = "Applicant name", example = "Jane Smith")
    private String userName;

    @Schema(description = "Applicant email", example = "jane.smith@example.com")
    private String userEmail;

    @Schema(description = "Timestamp when application was submitted", example = "2026-10-04T10:30:00")
    private LocalDateTime applicationDate;

    @Schema(description = "Current application status", example = "APPLIED")
    private ApplicationStatus status;

    public JobApplicationResponseDTO() {
    }

    public JobApplicationResponseDTO(Long id, Long jobId, String jobTitle, String company, Long userId, String userName, String userEmail, LocalDateTime applicationDate, ApplicationStatus status) {
        this.id = id;
        this.jobId = jobId;
        this.jobTitle = jobTitle;
        this.company = company;
        this.userId = userId;
        this.userName = userName;
        this.userEmail = userEmail;
        this.applicationDate = applicationDate;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public LocalDateTime getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(LocalDateTime applicationDate) {
        this.applicationDate = applicationDate;
    }

    public ApplicationStatus getStatus() {
        return status;
    }

    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }
}

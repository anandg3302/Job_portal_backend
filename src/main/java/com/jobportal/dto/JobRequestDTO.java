package com.jobportal.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

/**
 * Data Transfer Object for creating or updating a Job.
 */
@Schema(description = "Job Creation and Update Request")
public class JobRequestDTO {

    @NotBlank(message = "Job title is required")
    @Size(min = 3, max = 100, message = "Job title must be between 3 and 100 characters")
    @Schema(description = "Job title", example = "Senior Java Developer")
    private String title;

    @NotBlank(message = "Job description is required")
    @Size(min = 10, max = 2000, message = "Job description must be between 10 and 2000 characters")
    @Schema(description = "Detailed job description", example = "Looking for an experienced Spring Boot backend engineer...")
    private String description;

    @NotBlank(message = "Company name is required")
    @Schema(description = "Hiring company name", example = "TechCorp Solutions")
    private String company;

    @NotBlank(message = "Location is required")
    @Schema(description = "Job location or Remote", example = "Bangalore, India")
    private String location;

    @NotNull(message = "Salary is required")
    @Positive(message = "Salary must be greater than zero")
    @Schema(description = "Annual salary in USD or local currency", example = "85000.0")
    private Double salary;

    @NotNull(message = "Employer ID is required")
    @Schema(description = "ID of the employer user posting this job", example = "1")
    private Long employerId;

    public JobRequestDTO() {
    }

    public JobRequestDTO(String title, String description, String company, String location, Double salary, Long employerId) {
        this.title = title;
        this.description = description;
        this.company = company;
        this.location = location;
        this.salary = salary;
        this.employerId = employerId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public Long getEmployerId() {
        return employerId;
    }

    public void setEmployerId(Long employerId) {
        this.employerId = employerId;
    }
}

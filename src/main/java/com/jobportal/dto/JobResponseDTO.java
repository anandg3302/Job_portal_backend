package com.jobportal.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Data Transfer Object for Job responses.
 */
@Schema(description = "Job Details Response")
public class JobResponseDTO {

    @Schema(description = "Unique identifier of the job", example = "10")
    private Long id;

    @Schema(description = "Job title", example = "Senior Java Developer")
    private String title;

    @Schema(description = "Job description", example = "Looking for an experienced Spring Boot backend engineer...")
    private String description;

    @Schema(description = "Company name", example = "TechCorp Solutions")
    private String company;

    @Schema(description = "Location", example = "Bangalore, India")
    private String location;

    @Schema(description = "Offered salary", example = "85000.0")
    private Double salary;

    @Schema(description = "ID of the posting employer", example = "1")
    private Long employerId;

    @Schema(description = "Name of the posting employer", example = "TechCorp HR")
    private String employerName;

    public JobResponseDTO() {
    }

    public JobResponseDTO(Long id, String title, String description, String company, String location, Double salary, Long employerId, String employerName) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.company = company;
        this.location = location;
        this.salary = salary;
        this.employerId = employerId;
        this.employerName = employerName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getEmployerName() {
        return employerName;
    }

    public void setEmployerName(String employerName) {
        this.employerName = employerName;
    }
}

# Job Portal Web Application Backend

A clean, beginner-friendly, and production-ready **Job Portal Web Application Backend** built using **Java 17+, Spring Boot 3, PostgreSQL, Spring Data JPA, and REST APIs**.

---

## Table of Contents

- [Overview](#overview)
- [Key Features](#key-features)
- [Technology Stack](#technology-stack)
- [Project Architecture & Structure](#project-architecture--structure)
- [Database Schema & JPA Relationships](#database-schema--jpa-relationships)
- [PostgreSQL Database Setup](#postgresql-database-setup)
- [Configuration (`application.properties`)](#configuration-applicationproperties)
- [Step-by-Step Instructions to Run Locally](#step-by-step-instructions-to-run-locally)
- [API Documentation & Endpoints](#api-documentation--endpoints)
  - [User Management Endpoints](#1-user-management-endpoints)
  - [Job Management Endpoints](#2-job-management-endpoints)
  - [Job Application Endpoints](#3-job-application-endpoints)
- [Swagger / OpenAPI Documentation](#swagger--openapi-documentation)
- [Running Unit & Integration Tests](#running-unit--integration-tests)
- [Validation & Error Handling](#validation--error-handling)

---

## Overview

This application serves as the core backend for an online job board platform. It enables:
1. **Employers** to register, publish job postings, update listings, and review incoming applications.
2. **Job Seekers** to register, explore available positions, search vacancies by title/location, and submit applications.
3. **Application Tracking** to transition application statuses across `APPLIED`, `SHORTLISTED`, `REJECTED`, and `SELECTED`.

---

## Key Features

- **Layered Architecture:** Clear separation of concerns (`Controller` -> `Service` -> `Repository` -> `Database`).
- **RESTful Best Practices:** Consistent HTTP methods (`POST`, `GET`, `PUT`, `DELETE`), HTTP status codes (`201 Created`, `200 OK`, `400 Bad Request`, `404 Not Found`), and standardized JSON responses.
- **DTO Pattern:** Complete decoupling between database entities and API request/response payloads to eliminate recursion and protect sensitive data (such as passwords).
- **Relational Integrity:** Foreign key relationships enforcing 1-to-Many mappings (Employer -> Jobs, Job -> Applications, User -> Applications).
- **Search Functionality:** Flexible case-insensitive search by job title and/or location.
- **Input Validation:** Field validation using Jakarta Bean Validation (`@NotBlank`, `@Email`, `@Size`, `@NotNull`, `@Positive`).
- **Global Exception Handling:** Centralized `@RestControllerAdvice` generating standardized error envelopes with validation error maps.
- **Interactive Documentation:** Live Swagger UI / OpenAPI 3 specification for testing all endpoints directly in the browser.
- **Automated Testing:** 29 comprehensive unit and integration tests using JUnit 5, Mockito, and Spring MockMvc.

---

## Technology Stack

| Technology | Purpose |
| :--- | :--- |
| **Java 17+** | Programming Language |
| **Spring Boot 3.3.4** | Backend Framework |
| **Spring Data JPA / Hibernate** | Object-Relational Mapping (ORM) and Persistence |
| **PostgreSQL** | Primary Relational Database |
| **H2 Database** | In-Memory Database for automated tests |
| **Spring Validation (Hibernate Validator)** | Request DTO validation |
| **SpringDoc OpenAPI (Swagger UI)** | API documentation and sandbox |
| **JUnit 5 & Mockito** | Unit and Controller test suites |
| **Maven** | Build and dependency management tool |

---

## Project Architecture & Structure

```
d:\job_portal
├── pom.xml                                     # Maven build configuration & dependencies
├── README.md                                   # Complete documentation
├── mvnw / mvnw.cmd                             # Maven Wrapper scripts
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com
│   │   │       └── jobportal
│   │   │           ├── JobPortalApplication.java       # Spring Boot main class
│   │   │           ├── config
│   │   │           │   └── OpenApiConfig.java          # Swagger OpenAPI configuration
│   │   │           ├── controller
│   │   │           │   ├── UserController.java         # User REST endpoints
│   │   │           │   ├── JobController.java          # Job REST endpoints
│   │   │           │   └── JobApplicationController.java # Application REST endpoints
│   │   │           ├── dto
│   │   │           │   ├── ApiResponse.java            # Standard generic API wrapper
│   │   │           │   ├── UserRequestDTO.java         # User create/update payload
│   │   │           │   ├── UserResponseDTO.java        # User response payload
│   │   │           │   ├── JobRequestDTO.java          # Job create/update payload
│   │   │           │   ├── JobResponseDTO.java         # Job response payload
│   │   │           │   ├── JobApplicationRequestDTO.java # Apply job payload
│   │   │           │   ├── JobApplicationResponseDTO.java# Application response payload
│   │   │           │   └── JobApplicationStatusUpdateDTO.java # Status update payload
│   │   │           ├── entity
│   │   │           │   ├── Role.java                   # Enum: JOB_SEEKER, EMPLOYER
│   │   │           │   ├── ApplicationStatus.java      # Enum: APPLIED, SHORTLISTED, REJECTED, SELECTED
│   │   │           │   ├── User.java                   # JPA entity for users
│   │   │           │   ├── Job.java                    # JPA entity for jobs
│   │   │           │   └── JobApplication.java         # JPA entity for job applications
│   │   │           ├── exception
│   │   │           │   ├── ResourceNotFoundException.java # 404 Exception
│   │   │           │   ├── BadRequestException.java       # 400 Exception
│   │   │           │   ├── ErrorResponse.java             # Error response payload
│   │   │           │   └── GlobalExceptionHandler.java    # Centralized @RestControllerAdvice
│   │   │           ├── repository
│   │   │           │   ├── UserRepository.java         # Spring Data JPA User repository
│   │   │           │   ├── JobRepository.java          # Spring Data JPA Job repository
│   │   │           │   └── JobApplicationRepository.java# Spring Data JPA Application repository
│   │   │           └── service
│   │   │               ├── UserService.java            # User service interface
│   │   │               ├── JobService.java             # Job service interface
│   │   │               ├── JobApplicationService.java  # Application service interface
│   │   │               └── impl
│   │   │                   ├── UserServiceImpl.java    # User service implementation
│   │   │                   ├── JobServiceImpl.java     # Job service implementation
│   │   │                   └── JobApplicationServiceImpl.java # Application service impl
│   │   └── resources
│   │       └── application.properties          # PostgreSQL & server properties
│   └── test
│       ├── java
│       │   └── com
│       │       └── jobportal
│       │           ├── controller
│       │           │   ├── UserControllerTest.java     # MockMvc WebMvc tests
│       │           │   ├── JobControllerTest.java
│       │           │   └── JobApplicationControllerTest.java
│       │           └── service
│       │               ├── UserServiceTest.java        # JUnit 5 & Mockito service tests
│       │               ├── JobServiceTest.java
│       │               └── JobApplicationServiceTest.java
│       └── resources
│           └── application-test.properties     # H2 test properties
```

---

## Database Schema & JPA Relationships

```
 +--------------------+            +------------------------+
 |       users        | 1        * |          jobs          |
 +--------------------+------------+------------------------+
 | id (PK)            |            | id (PK)                |
 | name               |            | title                  |
 | email (UNIQUE)     |            | description            |
 | password           |            | company                |
 | role (ENUM)        |            | location               |
 +--------------------+            | salary                 |
           | 1                     | employer_id (FK:users) |
           |                       +------------------------+
           |                                   | 1
           | *                                 | *
 +----------------------------------------------------------+
 |                     job_applications                     |
 +----------------------------------------------------------+
 | id (PK)                                                  |
 | job_id (FK: jobs)                                        |
 | user_id (FK: users)                                      |
 | application_date (TIMESTAMP)                             |
 | status (ENUM: APPLIED, SHORTLISTED, REJECTED, SELECTED)  |
 +----------------------------------------------------------+
```

### Relational Rules:
1. **One Employer can post many Jobs (`User 1 -> * Job`)**:
   - `User.postedJobs` mapped by `Job.employer`.
   - Foreign key: `jobs.employer_id -> users.id`.
2. **One Job can have many Applications (`Job 1 -> * JobApplication`)**:
   - `Job.applications` mapped by `JobApplication.job`.
   - Foreign key: `job_applications.job_id -> jobs.id`.
3. **One Job Seeker can apply for many Jobs (`User 1 -> * JobApplication`)**:
   - `User.applications` mapped by `JobApplication.user`.
   - Foreign key: `job_applications.user_id -> users.id`.

---

## PostgreSQL Database Setup

### 1. Install PostgreSQL
Download and install PostgreSQL from the official website: [https://www.postgresql.org/download/](https://www.postgresql.org/download/)

### 2. Create the Database
Open **pgAdmin** or your terminal using `psql` and execute:

```sql
-- Connect to PostgreSQL server
psql -U postgres

-- Create the database
CREATE DATABASE job_portal_db;

-- Verify the database was created
\l
```

*(Hibernate will automatically create all tables and foreign key constraints on application startup via `spring.jpa.hibernate.ddl-auto=update`)*.

---

## Configuration (`application.properties`)

Check `src/main/resources/application.properties` and adjust your database credentials if different:

```properties
server.port=8080

# PostgreSQL Database Configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/job_portal_db
spring.datasource.username=postgres
spring.datasource.password=postgres
spring.datasource.driver-class-name=org.postgresql.Driver

# Spring Data JPA / Hibernate Settings
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

# Swagger / OpenAPI
springdoc.api-docs.path=/v3/api-docs
springdoc.swagger-ui.path=/swagger-ui.html
```

---

## Step-by-Step Instructions to Run Locally

### Prerequisites
- **JDK 17 or higher** installed (`java -version`).
- **PostgreSQL 12+** running on `localhost:5432`.
- **Maven** (or use the included `./mvnw` wrapper).

### Step 1: Clone or Navigate to Project Directory
```bash
cd d:/job_portal
```

### Step 2: Build the Application
```bash
# On Windows PowerShell:
.\mvnw.cmd clean package -DskipTests

# On Linux / macOS:
./mvnw clean package -DskipTests
```

### Step 3: Run the Application
```bash
# Using Maven:
.\mvnw.cmd spring-boot:run

# Or run the generated JAR:
java -jar target/job-portal-backend-1.0.0.jar
```

The application will start on port `8080`.

---

## API Documentation & Endpoints

All endpoints use JSON for requests and responses. Standard response structure:
```json
{
  "success": true,
  "message": "Operation successful",
  "data": { ... },
  "timestamp": "2026-10-04T12:00:00"
}
```

---

### 1. User Management Endpoints

| Method | Endpoint | Description | Status Code |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/users` | Register a new user (`JOB_SEEKER` or `EMPLOYER`) | `201 Created` |
| `GET` | `/api/users` | Retrieve all registered users | `200 OK` |
| `GET` | `/api/users/{id}` | Retrieve user details by ID | `200 OK` |
| `PUT` | `/api/users/{id}` | Update existing user details | `200 OK` |
| `DELETE` | `/api/users/{id}` | Delete a user account by ID | `200 OK` |

#### Sample Request: Register User (Employer)
`POST /api/users`
```json
{
  "name": "Acme HR",
  "email": "hr@acme.com",
  "password": "password123",
  "role": "EMPLOYER"
}
```

#### Sample Request: Register User (Job Seeker)
`POST /api/users`
```json
{
  "name": "Jane Doe",
  "email": "jane@example.com",
  "password": "password123",
  "role": "JOB_SEEKER"
}
```

---

### 2. Job Management Endpoints

| Method | Endpoint | Description | Status Code |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/jobs` | Create and post a new job (by employer) | `201 Created` |
| `GET` | `/api/jobs` | Retrieve all job listings | `200 OK` |
| `GET` | `/api/jobs/{id}` | Retrieve specific job details by ID | `200 OK` |
| `PUT` | `/api/jobs/{id}` | Update job details | `200 OK` |
| `DELETE` | `/api/jobs/{id}` | Delete a job posting | `200 OK` |
| `GET` | `/api/jobs/search?title=...&location=...` | Search jobs by title and/or location | `200 OK` |

#### Sample Request: Create Job
`POST /api/jobs`
```json
{
  "title": "Backend Java Engineer",
  "description": "Develop scalable REST APIs using Spring Boot and PostgreSQL.",
  "company": "Acme Corp",
  "location": "Bangalore",
  "salary": 95000.0,
  "employerId": 1
}
```

#### Sample Request: Search Jobs
`GET /api/jobs/search?title=Java&location=Bangalore`

---

### 3. Job Application Endpoints

| Method | Endpoint | Description | Status Code |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/applications` | Apply for a job (by job seeker) | `201 Created` |
| `GET` | `/api/applications` | Retrieve all submitted applications | `200 OK` |
| `GET` | `/api/applications/user/{userId}` | Retrieve all applications submitted by a user | `200 OK` |
| `GET` | `/api/applications/job/{jobId}` | Retrieve all applications for a particular job | `200 OK` |
| `PUT` | `/api/applications/{id}/status` | Update status of an application | `200 OK` |

#### Sample Request: Apply for a Job
`POST /api/applications`
```json
{
  "jobId": 1,
  "userId": 2
}
```

#### Sample Request: Update Application Status
`PUT /api/applications/1/status`
```json
{
  "status": "SHORTLISTED"
}
```
*(Valid statuses: `APPLIED`, `SHORTLISTED`, `REJECTED`, `SELECTED`)*

---

## Swagger / OpenAPI Documentation

Once the application is running, open your web browser and visit:

- **Swagger UI:** [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- **OpenAPI JSON Docs:** [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

From the Swagger UI interface, you can inspect schemas, request parameters, responses, and execute live API calls directly against the running application.

---

## Running Unit & Integration Tests

The test suite contains **29 unit and integration tests** testing all layers:
- Service layer unit tests with Mockito (`UserServiceTest`, `JobServiceTest`, `JobApplicationServiceTest`).
- Controller layer slice tests with MockMvc (`UserControllerTest`, `JobControllerTest`, `JobApplicationControllerTest`).
- Uses an embedded H2 database (`application-test.properties`) so tests run independently without needing a live PostgreSQL server.

To execute all tests:
```bash
# Windows:
.\mvnw.cmd test

# Linux / Mac:
./mvnw test
```

Expected output:
```
[INFO] Results:
[INFO] 
[INFO] Tests run: 29, Failures: 0, Errors: 0, Skipped: 0
[INFO] 
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
```

---

## Validation & Error Handling

Standardized error response returned for client errors:

```json
{
  "timestamp": "2026-10-04T12:00:00",
  "status": 400,
  "error": "Validation Failed",
  "message": "Input validation failed for one or more fields",
  "path": "/api/users",
  "validationErrors": {
    "email": "Invalid email format",
    "password": "Password must be at least 6 characters"
  }
}
```

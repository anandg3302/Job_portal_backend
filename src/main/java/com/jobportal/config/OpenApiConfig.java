package com.jobportal.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger / OpenAPI Configuration for API documentation and interactive testing.
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI jobPortalOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Job Portal Web Application REST API")
                        .description("Backend REST APIs for Job Portal application built with Spring Boot, PostgreSQL, and Spring Data JPA. " +
                                "Supports User Management, Job Postings & Search, and Job Applications tracking.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Job Portal Backend Team")
                                .email("support@jobportal.com")
                                .url("https://github.com/example/job-portal"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")));
    }
}

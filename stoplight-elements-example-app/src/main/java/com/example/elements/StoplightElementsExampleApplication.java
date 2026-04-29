package com.example.elements;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Example Spring Boot application demonstrating the Stoplight Elements starter.
 *
 * <p>Start the application and visit:
 * <ul>
 *   <li>{@code http://localhost:8080/api-docs} — Stoplight Elements API documentation</li>
 *   <li>{@code http://localhost:8080/v3/api-docs} — OpenAPI JSON spec</li>
 * </ul>
 */
@SpringBootApplication
public class StoplightElementsExampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(StoplightElementsExampleApplication.class, args);
    }
}

package com.airtel.assistant;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AssistantApplication {

    public static void main(String[] args) {
        // This is perfect. It starts the Web App only.
        SpringApplication.run(AssistantApplication.class, args);
    }
}
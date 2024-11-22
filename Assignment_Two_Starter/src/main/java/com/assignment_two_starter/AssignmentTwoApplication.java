package com.assignment_two_starter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication (exclude = {SecurityAutoConfiguration.class})
public class AssignmentTwoApplication {

    public static void main(String[] args) {
        SpringApplication.run(AssignmentTwoApplication.class, args);
    }
}

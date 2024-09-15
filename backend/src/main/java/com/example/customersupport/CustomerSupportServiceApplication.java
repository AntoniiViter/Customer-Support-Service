package com.example.customersupport;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = {"com.example.customersupport"})
@EnableScheduling  // Enable Spring Scheduling
public class CustomerSupportServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(CustomerSupportServiceApplication.class, args);
    }
}


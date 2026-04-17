package com.assignment.hardship;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class HardshipApplication {

    public static void main(String[] args) {
        SpringApplication.run(HardshipApplication.class, args);
    }

}

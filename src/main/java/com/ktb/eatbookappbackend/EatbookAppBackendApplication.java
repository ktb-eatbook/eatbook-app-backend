package com.ktb.eatbookappbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class EatbookAppBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(EatbookAppBackendApplication.class, args);
    }
}

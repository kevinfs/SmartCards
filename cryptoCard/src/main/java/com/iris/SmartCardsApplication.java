package com.iris;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class SmartCardsApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmartCardsApplication.class, args);
    }
}

package ru.verstache.gabella;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class })
public class Generator {
    public static void main(String[] args) {
        SpringApplication.run(Generator.class, args);
    }
}

package com.siuuuuu.backend;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class Application {
    @Value("${jasypt.encryptor.password}")
    private static String JASYPT_ENCRYPTOR_PASSWORD;

    public static void main(String[] args) {
        System.out.println("JASYPT_ENCRYPTOR_PASSWORD: " + JASYPT_ENCRYPTOR_PASSWORD);
        SpringApplication.run(Application.class, args);
    }
}

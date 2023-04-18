package com.example.cryptowatcher;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.IOException;


@EnableScheduling
@SpringBootApplication
public class CryptoWatcherApplication {
    public static void main(String[] args) throws IOException {
        EnvLoader.loadEnvVars();
        SpringApplication.run(CryptoWatcherApplication.class, args);
    }
}

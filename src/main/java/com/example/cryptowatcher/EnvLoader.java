package com.example.cryptowatcher;

import io.github.cdimascio.dotenv.Dotenv;

import java.io.IOException;


public class EnvLoader {

    public static void loadEnvVars() throws IOException {
        // Load environment variables from .env file
        Dotenv dotenv = Dotenv.load();

        // Set database properties from environment variables
        String databaseUrl = dotenv.get("DATABASE_URL");
        String databaseUser = dotenv.get("DATABASE_USER");
        String databasePassword = dotenv.get("DATABASE_PASSWORD");

        // Set database properties in application.properties file
        System.setProperty("spring.datasource.url", databaseUrl);
        System.setProperty("spring.datasource.username", databaseUser);
        System.setProperty("spring.datasource.password", databasePassword);
    }
}
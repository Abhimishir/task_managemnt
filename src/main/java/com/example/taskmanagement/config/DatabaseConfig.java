package com.example.taskmanagement.config;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfig {

    @Bean
    @Primary
    public DataSource dataSource() {
        // Try standard environment variables first
        String dbHost = getEnvWithFallback("DB_HOST", "POSTGRES_HOST", "DATABASE_HOST");
        String dbPort = getEnvWithFallback("DB_PORT", "POSTGRES_PORT", "DATABASE_PORT");
        String dbName = getEnvWithFallback("DB_NAME", "POSTGRES_DB", "DATABASE_NAME");
        String dbUser = getEnvWithFallback("DB_USER", "POSTGRES_USER", "DATABASE_USER");
        String dbPassword = getEnvWithFallback("DB_PASSWORD", "POSTGRES_PASSWORD", "DATABASE_PASSWORD");

        // Fallback to default values if environment variables are not set
        if (dbHost == null || dbHost.isEmpty()) {
            dbHost = "localhost";
        }
        if (dbPort == null || dbPort.isEmpty()) {
            dbPort = "5432";
        }
        if (dbName == null || dbName.isEmpty()) {
            dbName = "taskdb";
        }
        if (dbUser == null || dbUser.isEmpty()) {
            dbUser = "sa";
        }
        if (dbPassword == null) {
            dbPassword = "";
        }

        String jdbcUrl = "jdbc:postgresql://" + dbHost + ":" + dbPort + "/" + dbName;

        return DataSourceBuilder.create()
                .url(jdbcUrl)
                .username(dbUser)
                .password(dbPassword)
                .driverClassName("org.postgresql.Driver")
                .build();
    }

    private String getEnvWithFallback(String... keys) {
        for (String key : keys) {
            String value = System.getenv(key);
            if (value != null && !value.isEmpty()) {
                return value;
            }
        }
        return null;
    }
}

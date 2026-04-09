package com.example.taskmanagement.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfig {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseConfig.class);

    @Bean
    @Primary
    public DataSource dataSource() {
        // Try standard environment variables first
        String dbHost = getEnvWithFallback("DB_HOST", "POSTGRES_HOST", "DATABASE_HOST");
        String dbPort = getEnvWithFallback("DB_PORT", "POSTGRES_PORT", "DATABASE_PORT");
        String dbName = getEnvWithFallback("DB_NAME", "POSTGRES_DB", "DATABASE_NAME");
        String dbUser = getEnvWithFallback("DB_USER", "POSTGRES_USER", "DATABASE_USER");
        String dbPassword = getEnvWithFallback("DB_PASSWORD", "POSTGRES_PASSWORD", "DATABASE_PASSWORD");

        // Log the values (without password)
        logger.info("Database Configuration - Host: {}, Port: {}, Database: {}, User: {}",
                dbHost, dbPort, dbName, dbUser);

        // Fallback to default values if environment variables are not set
        if (dbHost == null || dbHost.isEmpty()) {
            logger.warn("DB_HOST not set, using default: localhost");
            dbHost = "localhost";
        }
        if (dbPort == null || dbPort.isEmpty()) {
            logger.warn("DB_PORT not set, using default: 5432");
            dbPort = "5432";
        }
        if (dbName == null || dbName.isEmpty()) {
            logger.warn("DB_NAME not set, using default: taskdb");
            dbName = "taskdb";
        }
        if (dbUser == null || dbUser.isEmpty()) {
            logger.warn("DB_USER not set, using default: sa");
            dbUser = "sa";
        }
        if (dbPassword == null) {
            logger.warn("DB_PASSWORD not set, using empty string");
            dbPassword = "";
        }

        String jdbcUrl = "jdbc:postgresql://" + dbHost + ":" + dbPort + "/" + dbName;
        logger.info("JDBC URL: {}", jdbcUrl);

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
                logger.debug("Found environment variable: {} = {}", key, value);
                return value;
            }
        }
        return null;
    }
}

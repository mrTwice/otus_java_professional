package ru.otus.java.professional.yampolskiy.work.with.datadase.configurations;

import java.io.IOException;
import java.util.Properties;

public class DatabaseConfig {
    private final String url;
    private final String user;
    private final String password;

    public DatabaseConfig() {
        Properties properties = ProjectProperties.getProjectProperties();
        this.url = properties.getProperty("database.url");
        this.user = properties.getProperty("database.user");
        this.password = properties.getProperty("database.password");

        validateConfig();
    }

    private void validateConfig() {
        if (url == null || url.isBlank()) {
            throw new IllegalArgumentException("Не указан 'database.url'");
        }
        if (user == null || user.isBlank()) {
            throw new IllegalArgumentException("Не указан 'database.user'");
        }
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Не указан 'database.password'");
        }
    }

    public String getUrl() {
        return url;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }
}
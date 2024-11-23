package ru.otus.java.professional.yampolskiy.patterns.two.configuration;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;

import static java.lang.System.getProperty;

public final class DataSource implements AutoCloseable {
    private static final Logger logger = LogManager.getLogger(DataSource.class);

    private static volatile DataSource INSTANCE;
    private final HikariDataSource hikariDataSource;
    private static final String DATABASE_URL = getProperty("database.url");
    private static final String POSTGRES_USER = getProperty("database.user");
    private static final String POSTGRES_PASSWORD = getProperty("database.password");

    private DataSource(DatabaseConfig config) {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(config.getUrl());
        hikariConfig.setUsername(config.getUser());
        hikariConfig.setPassword(config.getPassword());
        hikariConfig.setMaximumPoolSize(10);
        hikariConfig.setIdleTimeout(30000);
        hikariConfig.setConnectionTimeout(10000);
        this.hikariDataSource = new HikariDataSource(hikariConfig);
    }

    public static DataSource getInstance() {
        if (INSTANCE == null) {
            synchronized (DataSource.class) {
                if (INSTANCE == null) {
                    INSTANCE = new DataSource(new DatabaseConfig());
                }
            }
        }
        return INSTANCE;
    }

    public Connection getConnection() throws SQLException {
        return hikariDataSource.getConnection();
    }

    @Override
    public synchronized void close() {
        if (hikariDataSource != null) {
            hikariDataSource.close();
        }
    }
}

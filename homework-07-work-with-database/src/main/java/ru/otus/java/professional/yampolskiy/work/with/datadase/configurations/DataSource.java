package ru.otus.java.professional.yampolskiy.work.with.datadase.configurations;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static java.lang.System.getProperty;

public class DataSource {
    private static final Logger logger = LogManager.getLogger(DataSource.class);

    private static volatile DataSource INSTANCE;
    private final HikariDataSource hikariDataSource;

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

    public synchronized void close() {
        if (hikariDataSource != null) {
            hikariDataSource.close();
        }
    }
}

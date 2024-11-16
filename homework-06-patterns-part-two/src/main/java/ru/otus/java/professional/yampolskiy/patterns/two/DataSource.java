package ru.otus.java.professional.yampolskiy.patterns.two;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static java.lang.System.getProperty;

public final class DataSource {
    private final Logger logger = LogManager.getLogger(this);
    private static volatile DataSource INSTANCE;
    private static final String DATABASE_URL = getProperty("database.url");
    private static final String POSTGRES_USER = getProperty("database.user");
    private static final String POSTGRES_PASSWORD = getProperty("database.password");
    private Connection connection;

    private DataSource() throws SQLException {
        if (DATABASE_URL == null || POSTGRES_USER == null || POSTGRES_PASSWORD == null) {
            throw new IllegalArgumentException("Ошибка конфигурации базы данных, не указаны данные для подключения. Пожалуйста, проверьте следующие параметры: 'database.url', 'database.user', и 'database.password'.");
        }
        this.connection = DriverManager.getConnection(DATABASE_URL, POSTGRES_USER, POSTGRES_PASSWORD);
        logger.log(Level.INFO, "Соединение с базой данных открыто.");

    }

    public static DataSource getInstance() {
        if (INSTANCE == null) {
            synchronized (DataSource.class) {
                if (INSTANCE == null) {
                    try {
                        INSTANCE = new DataSource();
                    } catch (SQLException e) {
                        throw new RuntimeException("Ошибка создания подключения к базе данных.", e);
                    }
                }
            }
        }
        return INSTANCE;
    }

    public synchronized Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(DATABASE_URL, POSTGRES_USER, POSTGRES_PASSWORD);
            logger.log(Level.INFO, "Соединение с базой данных создано заново.");

        }
        return connection;
    }

    public synchronized void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                logger.log(Level.INFO, "Соединение с базой данных закрыто.");
            }
        } catch (SQLException e) {
            logger.error("Ошибка закрытия соединения.", e);
        }
    }
}

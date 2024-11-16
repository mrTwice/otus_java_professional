package ru.otus.java.professional.yampolskiy.patterns.two.transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.otus.java.professional.yampolskiy.patterns.two.configuration.DataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class TransactionManager {
    private final Logger logger = LogManager.getLogger(TransactionManager.class);
    private final DataSource dataSource;

    public TransactionManager(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public <T> T executeInTransaction(TransactionAction<T> action) {
        try (Connection connection = dataSource.getConnection()) {
            try {
                connection.setAutoCommit(false);
                logger.info("AutoCommit отключен");

                T result = action.execute(connection);

                connection.commit();
                return result;
            } catch (Exception e) {
                connection.rollback();
                throw new RuntimeException("Ошибка в транзакции", e);
            } finally {
                connection.setAutoCommit(true);
                logger.info("AutoCommit включен");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка подключения к базе данных", e);
        }
    }

    @FunctionalInterface
    public interface TransactionAction<T> {
        T execute(Connection connection) throws Exception;
    }
}

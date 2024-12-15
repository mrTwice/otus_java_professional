package ru.otus.java.professional.yampolskiy.jpql.configurations.migration;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.otus.java.professional.yampolskiy.jpql.configurations.propperties.ApplicationProperties;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static ru.otus.java.professional.yampolskiy.jpql.configurations.propperties.ApplicationProperties.*;

public class LiquibaseMigration {
    private static final Logger LOGGER = LogManager.getLogger(LiquibaseMigration.class);

    private LiquibaseMigration() {
    }

    public static void runMigrations(LiquibaseProperties liquibaseProperties) throws SQLException, LiquibaseException {
        LOGGER.info("Запуск миграции...");

        setSystemProperty(liquibaseProperties, LIQUIBASE_HUB_MODE, "off");
        setSystemProperty(liquibaseProperties, LIQUIBASE_STATS, "false");
        setSystemProperty(liquibaseProperties, LIQUIBASE_ANALYTICS, "false");

        try (Connection connection = DriverManager.getConnection(
                liquibaseProperties.getPropertyValue(DB_URL),
                liquibaseProperties.getPropertyValue(DB_USER),
                liquibaseProperties.getPropertyValue(DB_PASSWORD))) {

            Database database = DatabaseFactory.getInstance()
                    .findCorrectDatabaseImplementation(new JdbcConnection(connection));

            Liquibase liquibase = new Liquibase(
                    liquibaseProperties.getPropertyValue(LIQUIBASE_CHANGELOG),
                    new ClassLoaderResourceAccessor(),
                    database);

            liquibase.update();
            LOGGER.info("Миграция выполнена успешно.");
        } catch (Exception e) {
            LOGGER.error("Ошибка выполнения миграции", e);
            throw e;
        }
    }

    private static void setSystemProperty(LiquibaseProperties liquibaseProperties, ApplicationProperties property, String defaultValue) {
        if (liquibaseProperties.isContainProperty(property)) {
            System.setProperty(property.getKey(), liquibaseProperties.getPropertyValue(property));
        } else {
            LOGGER.warn("Свойство {} не найдено в файле конфигурации, используется значение по умолчанию '{}'.", property.getKey(), defaultValue);
            System.setProperty(property.getKey(), defaultValue);
        }
    }
}

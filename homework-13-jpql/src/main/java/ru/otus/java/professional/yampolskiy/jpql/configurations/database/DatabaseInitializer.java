package ru.otus.java.professional.yampolskiy.jpql.configurations.database;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import liquibase.exception.LiquibaseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.otus.java.professional.yampolskiy.jpql.configurations.migration.LiquibaseMigration;
import ru.otus.java.professional.yampolskiy.jpql.configurations.migration.LiquibaseProperties;
import ru.otus.java.professional.yampolskiy.jpql.configurations.propperties.ApplicationProperties;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

public class DatabaseInitializer {
    private static final Logger LOGGER = LogManager.getLogger(DatabaseInitializer.class);
    private static final String ENTITY_PACKAGE = "ru.otus.java.professional.yampolskiy.jpql.entities.";
    private final Properties properties;
    private final EntityManagerFactory entityManagerFactory;

    public DatabaseInitializer(Properties properties) {
        this.properties = properties;
        this.entityManagerFactory = Persistence.createEntityManagerFactory(
                properties.getProperty(ApplicationProperties.PERSISTENCE_UNIT.getKey()));
    }

    public void initializeDatabase() throws IOException, SQLException, LiquibaseException {
        LOGGER.info("Начало инициализации базы данных...");
        dropAllTables();
        runMigrations();
        logTableContents();

        LOGGER.info("Инициализация базы данных завершена.");
    }

    private void runMigrations() throws LiquibaseException, SQLException, IOException {
        LOGGER.info("Запуск миграций...");
        LiquibaseProperties liquibaseProperties = new LiquibaseProperties(properties);
        LiquibaseMigration.runMigrations(liquibaseProperties);
        LOGGER.info("Миграции выполнены.");
    }

    public void dropAllTables() {
        String sqlScript =
                "DO $$\n" +
                        "DECLARE\n" +
                        "    table_record RECORD;\n" +
                        "BEGIN\n" +
                        "    FOR table_record IN\n" +
                        "        (SELECT tablename FROM pg_tables WHERE schemaname = 'public')\n" +
                        "    LOOP\n" +
                        "        EXECUTE 'DROP TABLE IF EXISTS ' || quote_ident(table_record.tablename) || ' CASCADE';\n" +
                        "    END LOOP;\n" +
                        "END;\n" +
                        "$$;";

        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            EntityTransaction transaction = entityManager.getTransaction();
            transaction.begin();
            entityManager.createNativeQuery(sqlScript).executeUpdate();
            transaction.commit();
            LOGGER.info("Все таблицы успешно удалены.");
        } catch (Exception e) {
            LOGGER.error("Ошибка при удалении всех таблиц", e);
        }
    }

    private void logTableContents() {
        LOGGER.info("Логирование содержимого таблиц...");

        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            logEntityTable(entityManager, "Address");
            logEntityTable(entityManager, "Client");
            logEntityTable(entityManager, "Phone");
        } catch (Exception e) {
            LOGGER.error("Ошибка при логировании содержимого таблиц", e);
        }
    }

    private void logEntityTable(EntityManager entityManager, String entityClassName) {
        try {
            Class<?> entityClass = Class.forName(ENTITY_PACKAGE + entityClassName);
            List<?> resultList = entityManager.createQuery("SELECT e FROM " + entityClassName + " e", entityClass).getResultList();

            LOGGER.info("Таблица '{}' содержит {} записи:", entityClassName, resultList.size());
            resultList.forEach(entity -> LOGGER.info("{}: {}", entityClassName, entity));
        } catch (ClassNotFoundException e) {
            LOGGER.error("Класс сущности '{}' не найден", entityClassName, e);
        }
    }
}

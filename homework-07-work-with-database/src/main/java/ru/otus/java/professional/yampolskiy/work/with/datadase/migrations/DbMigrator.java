package ru.otus.java.professional.yampolskiy.work.with.datadase.migrations;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.otus.java.professional.yampolskiy.work.with.datadase.configurations.DataSource;
import ru.otus.java.professional.yampolskiy.work.with.datadase.configurations.ProjectProperties;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.stream.Collectors;

public class DbMigrator {
    private final String MIGRATIONS_PATH = ProjectProperties.getProjectProperties().getProperty("database.migration.path");;
    private final Logger logger = LogManager.getLogger(DbMigrator.class);
    private static DbMigrator INSTANCE;
    private final DataSource dataSource;

    private DbMigrator(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static DbMigrator getDbMigrator(DataSource dataSource) {
        if (INSTANCE == null) {
            synchronized (DataSource.class) {
                if (INSTANCE == null) {
                    INSTANCE = new DbMigrator(dataSource);
                }
            }
        }
        return INSTANCE;
    }

    private void initializeSchema() throws Exception {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);

            String checkSchemaQuery = """
            SELECT COUNT(*)
            FROM information_schema.tables
            WHERE table_name = 'schema_migrations'
            """;
            try (PreparedStatement preparedStatement = connection.prepareStatement(checkSchemaQuery);
                 ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next() && resultSet.getInt(1) > 0) {
                    logger.info("Схема уже инициализирована.");
                    return;
                }
            }

            logger.info("Схема не найдена. Выполняется инициализация...");
            String schemaScript = readMigrationScript("init_schema.sql");
            try (Statement statement = connection.createStatement()) {
                statement.execute(schemaScript);
            }
            connection.commit();
            logger.info("Схема успешно инициализирована.");
        } catch (Exception e) {
            logger.error("Ошибка при инициализации схемы: ", e);
            throw e;
        }
    }

    public void migrate() {
        try {
            initializeSchema();

            try (Connection connection = dataSource.getConnection()) {
                connection.setAutoCommit(false);

                List<String> migrationFiles = getMigrationFiles();
                for (String migrationFile : migrationFiles) {
                    if (migrationFile.equals("init_schema.sql")) {
                        continue;
                    }

                    String migrationScript = readMigrationScript(migrationFile);
                    String checksum = calculateChecksum(migrationScript);

                    if (!isMigrationExecuted(connection, migrationFile, checksum)) {
                        executeMigration(connection, migrationScript, migrationFile, checksum);
                    }
                }

                connection.commit();
            }
        } catch (Exception e) {
            logger.error("Ошибка во время выполнения миграции: ", e);
        }
    }


    private boolean isMigrationExecuted(Connection connection, String migrationName, String checksum) throws Exception {
        String checkQuery = """
                SELECT checksum
                FROM schema_migrations
                WHERE migration_name = ?
                """;
        try (PreparedStatement preparedStatement = connection.prepareStatement(checkQuery)) {
            preparedStatement.setString(1, migrationName);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String existingChecksum = resultSet.getString("checksum");
                    if (!existingChecksum.equals(checksum)) {
                        throw new IllegalStateException(
                                "Контрольная сумма файла миграции '%s' не совпадает. "
                                        + "Ожидаемая: %s, полученная: %s"
                                        .formatted(migrationName, existingChecksum, checksum));
                    }
                    return true;
                }
            }
        }
        return false;
    }

    private void executeMigration(Connection connection, String migrationScript, String migrationName, String checksum) throws Exception {
        try (Statement statement = connection.createStatement()) {
            statement.execute(migrationScript);
            logMigration(connection, migrationName, checksum);
            logger.info("Миграция выполнена: {}", migrationName);
        }
    }

    private void logMigration(Connection connection, String migrationName, String checksum) throws Exception {
        String insertQuery = """
                INSERT INTO schema_migrations (migration_name, checksum)
                VALUES (?, ?)
                """;
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            preparedStatement.setString(1, migrationName);
            preparedStatement.setString(2, checksum);
            preparedStatement.executeUpdate();
        }
    }

    private List<String> getMigrationFiles() throws Exception {
        var classLoader = getClass().getClassLoader();
        var resource = classLoader.getResource(MIGRATIONS_PATH);
        if (resource == null) {
            throw new IllegalArgumentException("Папка с миграциями не найдена: " + MIGRATIONS_PATH);
        }

        try (var stream = classLoader.getResourceAsStream(MIGRATIONS_PATH)) {
            if (stream == null) {
                throw new IllegalArgumentException("Не удалось прочитать миграции из: " + MIGRATIONS_PATH);
            }

            return new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))
                    .lines()
                    .filter(line -> line.endsWith(".sql"))
                    .sorted()
                    .collect(Collectors.toList());
        }
    }

    private String readMigrationScript(String migrationFile) throws Exception {
        var classLoader = getClass().getClassLoader();
        try (InputStream inputStream = classLoader.getResourceAsStream(MIGRATIONS_PATH + migrationFile)) {

            if (inputStream != null) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                    return reader.lines().collect(Collectors.joining("\n"));
                }
            }
        }
    }

    private String calculateChecksum(String script) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(script.getBytes(StandardCharsets.UTF_8));
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

}

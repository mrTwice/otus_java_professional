package ru.otus.java.professional.yampolskiy.work.with.datadase.configurations;

import ru.otus.java.professional.yampolskiy.work.with.datadase.migrations.DbMigrator;

import java.io.IOException;
import java.util.Properties;

public class ProjectProperties {
    private static ProjectProperties INSTANCE;
    private final Properties properties;

    private ProjectProperties(Properties properties) {
        this.properties =  properties;
        try (var inputStream = getClass().getClassLoader().getResourceAsStream("application.properties")) {
            if (inputStream == null) {
                throw new IllegalArgumentException("Файл application.properties не найден");
            }
            properties.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка загрузки настроек", e);
        }
    }




    public static Properties getProjectProperties() {
        if (INSTANCE == null) {
            synchronized (DataSource.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ProjectProperties(new Properties());
                }
            }
        }
        return INSTANCE.properties;
    }
}

package ru.otus.java.professional.yampolskiy.work.with.datadase.migrations;

import ru.otus.java.professional.yampolskiy.work.with.datadase.configurations.DataSource;

public class DbMigrator {
    private DataSource dataSource;

    public DbMigrator(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void migrate() {
        // читаем файл dbinit.sql
        // выполняем все запросы, чтобы проинициализировать БД
    }
}

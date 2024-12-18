package ru.otus.java.professional.yampolskiy.work.with.datadase.services;

import ru.otus.java.professional.yampolskiy.work.with.datadase.configurations.DataSource;

import java.sql.SQLException;

public class BonusService {
    private DataSource dataSource;

    public BonusService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void init() throws SQLException {
        dataSource.getConnection().createStatement().executeUpdate(
                "" +
                        "create table if not exists bonuses (" +
                        "    id          bigserial primary key," +
                        "    amount      int," +
                        "    login       varchar(255)" +
                        ")"
        );
        System.out.println("Сервис бонусов успешно запущен");
    }

    public void createBonus(String login, int amount) {
        // dataSource.getStatement()...
    }
}

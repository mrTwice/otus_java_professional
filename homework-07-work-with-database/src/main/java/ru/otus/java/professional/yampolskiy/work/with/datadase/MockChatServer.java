package ru.otus.java.professional.yampolskiy.work.with.datadase;

import ru.otus.java.professional.yampolskiy.work.with.datadase.configurations.DataSource;
import ru.otus.java.professional.yampolskiy.work.with.datadase.entities.User;
import ru.otus.java.professional.yampolskiy.work.with.datadase.migrations.DbMigrator;
import ru.otus.java.professional.yampolskiy.work.with.datadase.repositories.AbstractRepository;
import ru.otus.java.professional.yampolskiy.work.with.datadase.repositories.UsersDao;

import java.sql.SQLException;

public class MockChatServer {
    public static void main(String[] args) throws SQLException {
        DbMigrator dbMigrator = DbMigrator.getDbMigrator(DataSource.getInstance());
        dbMigrator.migrate();

        System.out.println("Сервер чата запущен");
        UsersDao usersDao = new UsersDao(DataSource.getInstance());
        System.out.println(usersDao.getAllUsers());
            usersDao.save(new User(null, "A", "A", "A"));
//            System.out.println(usersDao.getAllUsers());
        AbstractRepository<User> usersRepository = new AbstractRepository<>(DataSource.getInstance(), User.class);
        usersRepository.save(new User(null, "B", "B", "B"));
        System.out.println(usersDao.getAllUsers());

//            AuthenticationService authenticationService = new AuthenticationService(usersDao);
//            UsersStatisticService usersStatisticService = new UsersStatisticService(usersDao);
//            BonusService bonusService = new BonusService(dataSource);
//            bonusService.init();

//            authenticationService.register("A", "A", "A");
        // Основная работа сервера чата

    }
}

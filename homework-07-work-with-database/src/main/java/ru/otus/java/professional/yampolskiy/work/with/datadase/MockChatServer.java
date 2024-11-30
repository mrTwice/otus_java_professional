package ru.otus.java.professional.yampolskiy.work.with.datadase;

import ru.otus.java.professional.yampolskiy.work.with.datadase.configurations.DataSource;
import ru.otus.java.professional.yampolskiy.work.with.datadase.entities.User;
import ru.otus.java.professional.yampolskiy.work.with.datadase.migrations.DbMigrator;
import ru.otus.java.professional.yampolskiy.work.with.datadase.repositories.AbstractRepository;
import ru.otus.java.professional.yampolskiy.work.with.datadase.repositories.UsersDao;

import java.sql.SQLException;
import java.util.List;

public class MockChatServer {
    public static void main(String[] args) throws SQLException {
        DbMigrator dbMigrator = DbMigrator.getDbMigrator(DataSource.getInstance());
        dbMigrator.migrate();

        System.out.println("Сервер чата запущен");
        UsersDao usersDao = new UsersDao(DataSource.getInstance());
        System.out.println(usersDao.getAllUsers());
        usersDao.save(new User(null, "A", "A", "A"));

        AbstractRepository<User> usersRepository = new AbstractRepository<>(DataSource.getInstance(), User.class);
        System.out.println("Добавление пользователей...");
        usersRepository.save(new User(null, "User1", " Пароль1", "прозвище1"));
        usersRepository.save(new User(null, "User2", "Пароль2", "прозвище2"));

        System.out.println("Список всех пользователей:");
        List<User> allUsers = usersRepository.findAll();
        allUsers.forEach(System.out::println);

        if (!allUsers.isEmpty()) {
            User firstUser = allUsers.get(0);
            Long userId = firstUser.getId();
            System.out.println("Поиск пользователя с ID = " + userId);
            User foundUser = usersRepository.findById(userId);
            System.out.println("Найденный пользователь: " + foundUser);
        }

        if (!allUsers.isEmpty()) {
            User userToUpdate = allUsers.get(0);
            userToUpdate.setLogin("newLogin");
            userToUpdate.setPassword("newPassword");
            userToUpdate.setNickname("newNickname");
            System.out.println("Обновление пользователя: " + userToUpdate);
            usersRepository.update(userToUpdate);

            System.out.println("Список всех пользователей после обновления:");
            usersRepository.findAll().forEach(System.out::println);
        }

        if (!allUsers.isEmpty()) {
            User userToDelete = allUsers.get(0);
            System.out.println("Удаление пользователя с ID = " + userToDelete.getId());
            usersRepository.deleteById(userToDelete.getId());

            System.out.println("Список всех пользователей после удаления:");
            usersRepository.findAll().forEach(System.out::println);
        }
        System.out.println(usersDao.getAllUsers());

//            AuthenticationService authenticationService = new AuthenticationService(usersDao);
//            UsersStatisticService usersStatisticService = new UsersStatisticService(usersDao);
//            BonusService bonusService = new BonusService(dataSource);
//            bonusService.init();

//            authenticationService.register("A", "A", "A");
        // Основная работа сервера чата

    }
}

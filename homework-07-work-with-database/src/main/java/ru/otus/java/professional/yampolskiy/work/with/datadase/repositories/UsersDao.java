package ru.otus.java.professional.yampolskiy.work.with.datadase.repositories;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.otus.java.professional.yampolskiy.work.with.datadase.configurations.DataSource;
import ru.otus.java.professional.yampolskiy.work.with.datadase.entities.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class UsersDao {
    private final Logger logger = LogManager.getLogger(UsersDao.class);
    private DataSource dataSource;

    public UsersDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Optional<User> getUserByLoginAndPassword(String login, String password) {
        try (ResultSet rs = dataSource.getConnection().createStatement().executeQuery("select * from users where user_login = '" + login + "' AND password = '" + password + "'")) {
            return Optional.of(
                    new User(
                            rs.getLong("id"),
                            rs.getString("login"),
                            rs.getString("password"),
                            rs.getString("nickname")
                    )
            );
        } catch (SQLException e) {
            logger.error("Ошибка получения пользователя по логину", e);
        }
        return Optional.empty();
    }

    public Optional<User> getUserById(Long id) {
        try (ResultSet rs = dataSource.getConnection().createStatement().executeQuery("select * from users where id = " + id)) {
            if (rs.next()) {
                return Optional.of(
                        new User(
                                rs.getLong("id"),
                                rs.getString("login"),
                                rs.getString("password"),
                                rs.getString("nickname")
                        )
                );
            }
        } catch (SQLException e) {
            logger.error("Ошибка получения пользователя по id", e);

        }
        return Optional.empty();
    }

    public List<User> getAllUsers() {
        List<User> result = new ArrayList<>();
        try (ResultSet rs = dataSource.getConnection().createStatement().executeQuery("select * from users")) {
            while (rs.next()) {
                result.add(
                        new User(
                                rs.getLong("id"),
                                rs.getString("user_login"),
                                rs.getString("user_password"),
                                rs.getString("user_nickname"))
                );
            }
        } catch (SQLException e) {
            logger.error("Ошибка получения списка пользователей", e);

        }
        return Collections.unmodifiableList(result);
    }

    public void save(User user) throws SQLException {
        dataSource.getConnection()
                .createStatement()
                .executeUpdate(
                        String.format(
                                "insert into users (user_login, user_password, user_nickname) values ('%s', '%s', '%s');",
                                user.getLogin(),
                                user.getPassword(),
                                user.getNickname())
                );
    }

    public void saveAll(List<User> users) throws SQLException {
        dataSource.getConnection().setAutoCommit(false);
        for (User u : users) {
            dataSource.getConnection()
                    .createStatement()
                    .executeUpdate(
                            String.format(
                                    "insert into users (user_login, user_password, user_nickname) values ('%s', '%s', '%s');",
                                    u.getLogin(),
                                    u.getPassword(),
                                    u.getNickname())
                    );
        }
        dataSource.getConnection().setAutoCommit(true);
    }
}

package ru.otus.java.professional.yampolskiy.work.with.datadase.services;

import ru.otus.java.professional.yampolskiy.work.with.datadase.entities.User;
import ru.otus.java.professional.yampolskiy.work.with.datadase.repositories.UsersDao;

import java.sql.*;

public class AuthenticationService {
    private UsersDao usersDao;

    public AuthenticationService(UsersDao usersDao) {
        this.usersDao = usersDao;
    }

    public void register(String login, String password, String nickname) throws SQLException {
        usersDao.save(new User(null, login, password, nickname));
    }
}

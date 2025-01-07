package ru.otus.java.professional.yampolskiy.work.with.datadase.services;

import ru.otus.java.professional.yampolskiy.work.with.datadase.entities.User;
import ru.otus.java.professional.yampolskiy.work.with.datadase.repositories.UsersDao;

public class UsersStatisticService {
    private UsersDao usersDao;

    public UsersStatisticService(UsersDao usersDao) {
        this.usersDao = usersDao;
    }

    public void businessLogicMethod(Long id) {
        User user = usersDao.getUserById(id).get();
        // ... какая-то обработка данных по юзеру
        System.out.println(user);
    }
}

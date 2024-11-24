package ru.otus.java.professional.yampolskiy.work.with.datadase.entities;

import ru.otus.java.professional.yampolskiy.work.with.datadase.annotations.RepositoryField;
import ru.otus.java.professional.yampolskiy.work.with.datadase.annotations.RepositoryIdField;
import ru.otus.java.professional.yampolskiy.work.with.datadase.annotations.RepositoryTable;

@RepositoryTable(title = "users")
public class User {
    @RepositoryIdField
    private Long id;
    @RepositoryField(columnName = "user_login")
    private String login;
    @RepositoryField(columnName = "user_password")
    private String password;
    @RepositoryField(columnName = "user_nickname")
    private String nickname;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public User() {
    }

    public User(Long id, String login, String password, String nickname) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.nickname = nickname;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", nickname='" + nickname + '\'' +
                '}';
    }
}

package ru.otus.java.professional.yampolskiy.jpql.configurations.propperties.base;

import ru.otus.java.professional.yampolskiy.jpql.configurations.propperties.PropertyKey;

public enum DatabaseProperties implements PropertyKey {
    DB_URL("db.url"),
    DB_USER("db.user"),
    DB_PASSWORD("db.password");

    private final String key;

    DatabaseProperties(String key) {
        this.key = key;
    }

    @Override
    public String getKey() {
        return key;
    }
}


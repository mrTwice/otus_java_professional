package ru.otus.java.professional.yampolskiy.jpql.configurations.propperties.base;

import ru.otus.java.professional.yampolskiy.jpql.configurations.propperties.PropertyKey;

public enum ApplicationProperties implements PropertyKey {
    APP_NAME("app.name"),
    APP_VERSION("app.version");

    private final String key;

    ApplicationProperties(String key) {
        this.key = key;
    }

    @Override
    public String getKey() {
        return key;
    }
}


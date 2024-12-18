package ru.otus.java.professional.yampolskiy.jpql.configurations.propperties;

public enum ApplicationProperties implements PropertyKey {
    APP_NAME("app.name"),
    APP_VERSION("app.version"),
    DB_URL("db.url"),
    DB_USER("db.user"),
    DB_PASSWORD("db.password"),
    LIQUIBASE_CHANGELOG("liquibase.changelog"),
    LIQUIBASE_HUB_MODE("liquibase.hub.mode"),
    LIQUIBASE_STATS("liquibase.shouldSendUsageStats"),
    LIQUIBASE_ANALYTICS("liquibase.analytics.enabled"),
    PERSISTENCE_UNIT("Homework13Persistence"),

    ;


    private final String key;

    ApplicationProperties(String key) {
        this.key = key;
    }

    @Override
    public String getKey() {
        return key;
    }
}


package ru.otus.java.professional.yampolskiy.jpql.configurations.migration;

import lombok.Getter;
import ru.otus.java.professional.yampolskiy.jpql.configurations.propperties.ApplicationProperties;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Getter
public class LiquibaseProperties {
    private final Map<String, String> propertiesMap;

    public LiquibaseProperties(Properties properties) {
        propertiesMap = new HashMap<>();
        propertiesMap.put(ApplicationProperties.DB_URL.getKey(), properties.getProperty(ApplicationProperties.DB_URL.getKey()));
        propertiesMap.put(ApplicationProperties.DB_USER.getKey(), properties.getProperty(ApplicationProperties.DB_USER.getKey()));
        propertiesMap.put(ApplicationProperties.DB_PASSWORD.getKey(), properties.getProperty(ApplicationProperties.DB_PASSWORD.getKey()));
        propertiesMap.put(ApplicationProperties.LIQUIBASE_CHANGELOG.getKey(), properties.getProperty(ApplicationProperties.LIQUIBASE_CHANGELOG.getKey()));
        propertiesMap.put(ApplicationProperties.LIQUIBASE_HUB_MODE.getKey(), properties.getProperty(ApplicationProperties.LIQUIBASE_HUB_MODE.getKey()));
        propertiesMap.put(ApplicationProperties.LIQUIBASE_STATS.getKey(), properties.getProperty(ApplicationProperties.LIQUIBASE_STATS.getKey()));
    }

    public String getPropertyValue(ApplicationProperties property) {
        return propertiesMap.get(property.getKey());
    }

    public Map<String, String> getPropertiesMap() {
        return propertiesMap;
    }

    public boolean isContainProperty(ApplicationProperties property) {
        return propertiesMap.containsKey(property.getKey());
    }

    @Override
    public String toString() {
        return "LiquibaseProperties{" +
                "propertiesMap=" + propertiesMap +
                '}';
    }
}


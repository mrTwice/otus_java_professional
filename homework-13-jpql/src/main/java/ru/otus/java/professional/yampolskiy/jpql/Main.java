package ru.otus.java.professional.yampolskiy.jpql;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import ru.otus.java.professional.yampolskiy.jpql.configurations.migration.LiquibaseMigration;
import ru.otus.java.professional.yampolskiy.jpql.configurations.migration.LiquibaseProperties;
import ru.otus.java.professional.yampolskiy.jpql.configurations.propperties.ApplicationProperties;
import ru.otus.java.professional.yampolskiy.jpql.configurations.propperties.PropertiesReader;

import java.util.Properties;

import static jakarta.persistence.Persistence.createEntityManagerFactory;


public class Main {
    public static void main(String[] args) throws Exception {
        Properties properties = new PropertiesReader().getProperties();
        LiquibaseProperties liquibaseProperties = new LiquibaseProperties(properties);
        LiquibaseMigration.runMigrations(liquibaseProperties);

        try (EntityManagerFactory entityManagerFactory = createEntityManagerFactory(properties.getProperty(ApplicationProperties.PERSISTENCE_UNIT.getKey()));
             EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            System.out.println("Нечто");
        }


    }
}

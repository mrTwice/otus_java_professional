package ru.otus.java.professional.yampolskiy.jpql;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import liquibase.exception.LiquibaseException;
import ru.otus.java.professional.yampolskiy.jpql.configurations.database.DatabaseInitializer;
import ru.otus.java.professional.yampolskiy.jpql.configurations.propperties.ApplicationProperties;
import ru.otus.java.professional.yampolskiy.jpql.configurations.propperties.PropertiesReader;
import ru.otus.java.professional.yampolskiy.jpql.entities.Address;
import ru.otus.java.professional.yampolskiy.jpql.entities.Client;
import ru.otus.java.professional.yampolskiy.jpql.entities.Phone;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;

import static jakarta.persistence.Persistence.createEntityManagerFactory;


public class Main {
    private static final Properties properties;
    private static final DatabaseInitializer databaseInitializer;
    private static EntityManagerFactory entityManagerFactory;

    static {
        try {
            properties = new PropertiesReader().getProperties();
            entityManagerFactory = createEntityManagerFactory(properties.getProperty(ApplicationProperties.PERSISTENCE_UNIT.getKey()));
            databaseInitializer = new DatabaseInitializer(properties);
            databaseInitializer.initializeDatabase();
        } catch (IOException | SQLException | LiquibaseException e) {
            throw new RuntimeException(e);
        }
    }



    public static void main(String[] args) throws Exception {

        createClient("Bob","Main street", "987654321");
        System.out.println(getClient(1L));
        updateAddressAndPhoneClient(1L, "New Street", "123456789");
    }

    private static void createClient(String name, String street, String phoneNumber) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            Client client = new Client(name, new Address(street));
            client.addPhone(new Phone(phoneNumber));
            EntityTransaction transaction = entityManager.getTransaction();
            transaction.begin();
            entityManager.persist(client);
            transaction.commit();
        }
    }

    private static Client getClient(Long id) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            return entityManager.find(Client.class, id);
        }
    }

    private static void updateAddressAndPhoneClient(Long id, String street, String phoneNumber) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            EntityTransaction transaction = entityManager.getTransaction();
            transaction.begin();
            Client client = entityManager.find(Client.class, id);
            if (client != null) {
                throw new RuntimeException("Client with id " + id + " not found");
            }
            client.addPhone(new Phone(phoneNumber));
            client.setAddress(new Address(street));
            transaction.commit();
        }
    }

}

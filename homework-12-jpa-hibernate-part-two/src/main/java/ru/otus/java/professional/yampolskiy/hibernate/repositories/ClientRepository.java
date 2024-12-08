package ru.otus.java.professional.yampolskiy.hibernate.repositories;

import org.hibernate.SessionFactory;
import ru.otus.java.professional.yampolskiy.hibernate.entities.Client;

public class ClientRepository extends AbstractRepository<Client, Long> {
    public ClientRepository(SessionFactory sessionFactory) {
        super(Client.class, sessionFactory);
    }
}

package ru.otus.java.professional.yampolskiy.hibernate.services;

import ru.otus.java.professional.yampolskiy.hibernate.entities.Client;
import ru.otus.java.professional.yampolskiy.hibernate.repositories.ClientRepository;

public class ClientServiceImpl extends AbstractService<Client, Long> implements ClientService {
    public ClientServiceImpl(ClientRepository repository) {
        super(repository);
    }

}

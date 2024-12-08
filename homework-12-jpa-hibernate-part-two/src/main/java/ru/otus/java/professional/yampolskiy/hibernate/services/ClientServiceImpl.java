package ru.otus.java.professional.yampolskiy.hibernate.services;

import ru.otus.java.professional.yampolskiy.hibernate.entities.Client;
import ru.otus.java.professional.yampolskiy.hibernate.entities.Product;
import ru.otus.java.professional.yampolskiy.hibernate.repositories.interfaces.ClientRepository;
import ru.otus.java.professional.yampolskiy.hibernate.repositories.ClientRepositoryImpl;
import ru.otus.java.professional.yampolskiy.hibernate.services.interfaces.ClientService;

import java.util.List;


public class ClientServiceImpl extends AbstractService<Client, Long> implements ClientService {
    private final ClientRepository clientRepository;
    public ClientServiceImpl(ClientRepositoryImpl repository) {
        super(repository);
        this.clientRepository = repository;
    }

    public Client getClientPurchasesWithDetails(Long clientId) {
        return clientRepository.findClientPurchasesWithDetails(clientId);
    }

    @Override
    public List<Product> getOrderingProductsByClientId(Long clientId){
        return clientRepository.getOrderingProductsByClientId(clientId);
    }

    @Override
    public Client findByLogin(String login) {
        return clientRepository.findByLogin(login);
    }

}

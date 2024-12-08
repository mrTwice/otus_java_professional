package ru.otus.java.professional.yampolskiy.hibernate.repositories.interfaces;

import ru.otus.java.professional.yampolskiy.hibernate.entities.Client;
import ru.otus.java.professional.yampolskiy.hibernate.entities.Product;

import java.util.List;

public interface ClientRepository extends Repository<Client, Long> {
    public Client findClientPurchasesWithDetails(Long clientId);
    List<Product> getOrderingProductsByClientId(Long clientId);
    Client findByLogin(String login);
}

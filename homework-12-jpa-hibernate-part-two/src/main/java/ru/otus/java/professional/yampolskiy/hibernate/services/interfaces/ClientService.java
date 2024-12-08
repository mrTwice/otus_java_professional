package ru.otus.java.professional.yampolskiy.hibernate.services.interfaces;

import ru.otus.java.professional.yampolskiy.hibernate.entities.Client;
import ru.otus.java.professional.yampolskiy.hibernate.entities.Product;

import java.util.List;

public interface ClientService extends Service<Client, Long> {
    List<Product> getOrderingProductsByClientId(Long clientId);
    Client findByLogin(String login);
}

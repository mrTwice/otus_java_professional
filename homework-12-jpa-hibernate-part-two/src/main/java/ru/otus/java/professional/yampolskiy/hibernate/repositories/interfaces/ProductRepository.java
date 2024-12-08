package ru.otus.java.professional.yampolskiy.hibernate.repositories.interfaces;

import ru.otus.java.professional.yampolskiy.hibernate.entities.Client;
import ru.otus.java.professional.yampolskiy.hibernate.entities.PriceHistory;
import ru.otus.java.professional.yampolskiy.hibernate.entities.Product;

import java.util.List;

public interface ProductRepository extends Repository<Product, Long> {
    public Product findByName(String name);
    List<PriceHistory> findPriceHistoryById(Long productId);
    List<Client> findClientsByProductId(Long productId);

}

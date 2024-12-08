package ru.otus.java.professional.yampolskiy.hibernate.services.interfaces;

import ru.otus.java.professional.yampolskiy.hibernate.entities.Client;
import ru.otus.java.professional.yampolskiy.hibernate.entities.PriceHistory;
import ru.otus.java.professional.yampolskiy.hibernate.entities.Product;

import java.util.List;


public interface ProductService extends Service<Product, Long> {
    Product findByName(String name);
    List<PriceHistory> findPriceHistoryById(Long productId);
    List<Client> findClientsByProductId(Long productId);
}

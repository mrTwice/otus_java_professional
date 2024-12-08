package ru.otus.java.professional.yampolskiy.hibernate.services;

import ru.otus.java.professional.yampolskiy.hibernate.entities.Client;
import ru.otus.java.professional.yampolskiy.hibernate.entities.PriceHistory;
import ru.otus.java.professional.yampolskiy.hibernate.entities.Product;
import ru.otus.java.professional.yampolskiy.hibernate.repositories.interfaces.ProductRepository;
import ru.otus.java.professional.yampolskiy.hibernate.repositories.ProductRepositoryImpl;
import ru.otus.java.professional.yampolskiy.hibernate.services.interfaces.ProductService;

import java.util.List;

public class ProductServiceImpl extends AbstractService<Product, Long> implements ProductService {
    private final ProductRepository repository;
    public ProductServiceImpl(ProductRepositoryImpl productRepositoryImpl) {
        super(productRepositoryImpl);
        this.repository = productRepositoryImpl;
    }

    public Product findByName(String title) {
        return repository.findByName(title);
    }

    @Override
    public List<PriceHistory> findPriceHistoryById(Long productId) {
        return repository.findPriceHistoryById(productId);
    }

    @Override
    public List<Client> findClientsByProductId(Long productId){
        return repository.findClientsByProductId(productId);
    }

}

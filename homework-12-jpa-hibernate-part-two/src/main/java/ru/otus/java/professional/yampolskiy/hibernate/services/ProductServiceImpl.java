package ru.otus.java.professional.yampolskiy.hibernate.services;

import ru.otus.java.professional.yampolskiy.hibernate.entities.Product;
import ru.otus.java.professional.yampolskiy.hibernate.repositories.ProductRepository;

public class ProductServiceImpl extends AbstractService<Product,Long> implements ProductService {
    public ProductServiceImpl(ProductRepository productRepository) {
        super(productRepository);
    }
}

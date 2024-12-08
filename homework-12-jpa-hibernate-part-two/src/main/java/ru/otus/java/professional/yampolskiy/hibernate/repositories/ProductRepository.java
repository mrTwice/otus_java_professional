package ru.otus.java.professional.yampolskiy.hibernate.repositories;

import org.hibernate.SessionFactory;
import ru.otus.java.professional.yampolskiy.hibernate.entities.Product;

public class ProductRepository extends AbstractRepository<Product, Long> {
    public ProductRepository(SessionFactory sessionFactory) {
        super(Product.class, sessionFactory);
    }
}

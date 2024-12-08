package ru.otus.java.professional.yampolskiy.hibernate.repositories;

import org.hibernate.SessionFactory;
import ru.otus.java.professional.yampolskiy.hibernate.entities.Product;

public class ProductRepository extends AbstractRepository<Long, Product> {
    public ProductRepository(Class<Long> entityClass, SessionFactory sessionFactory) {
        super(entityClass, sessionFactory);
    }
}

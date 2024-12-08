package ru.otus.java.professional.yampolskiy.hibernate.repositories;

import org.hibernate.SessionFactory;
import ru.otus.java.professional.yampolskiy.hibernate.entities.Order;

public class OrderRepository extends AbstractRepository<Long, Order> {
    public OrderRepository(Class<Long> entityClass, SessionFactory sessionFactory) {
        super(entityClass, sessionFactory);
    }
}

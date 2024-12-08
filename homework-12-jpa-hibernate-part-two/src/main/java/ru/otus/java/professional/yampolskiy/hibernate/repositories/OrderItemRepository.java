package ru.otus.java.professional.yampolskiy.hibernate.repositories;

import org.hibernate.SessionFactory;
import ru.otus.java.professional.yampolskiy.hibernate.entities.OrderItem;

public class OrderItemRepository extends AbstractRepository<Long, OrderItem> {
    public OrderItemRepository(Class<Long> entityClass, SessionFactory sessionFactory) {
        super(entityClass, sessionFactory);
    }
}

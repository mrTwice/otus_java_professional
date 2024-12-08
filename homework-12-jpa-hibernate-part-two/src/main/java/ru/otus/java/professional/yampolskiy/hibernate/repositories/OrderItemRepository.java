package ru.otus.java.professional.yampolskiy.hibernate.repositories;

import org.hibernate.SessionFactory;
import ru.otus.java.professional.yampolskiy.hibernate.entities.OrderItem;

public class OrderItemRepository extends AbstractRepository<OrderItem, Long> {
    public OrderItemRepository(SessionFactory sessionFactory) {
        super(OrderItem.class, sessionFactory);
    }
}

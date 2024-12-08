package ru.otus.java.professional.yampolskiy.hibernate.repositories;

import org.hibernate.SessionFactory;
import ru.otus.java.professional.yampolskiy.hibernate.entities.Order;

public class OrderRepository extends AbstractRepository<Order, Long> {
    public OrderRepository(SessionFactory sessionFactory) {
        super(Order.class, sessionFactory);
    }
}

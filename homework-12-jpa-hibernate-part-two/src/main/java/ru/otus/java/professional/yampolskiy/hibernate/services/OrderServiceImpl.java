package ru.otus.java.professional.yampolskiy.hibernate.services;

import ru.otus.java.professional.yampolskiy.hibernate.entities.Order;
import ru.otus.java.professional.yampolskiy.hibernate.repositories.OrderRepository;

public class OrderServiceImpl extends AbstractService<Order, Long> implements OrderService {
    public OrderServiceImpl(OrderRepository repository) {
        super(repository);
    }

}

package ru.otus.java.professional.yampolskiy.hibernate.services;

import ru.otus.java.professional.yampolskiy.hibernate.entities.OrderItem;
import ru.otus.java.professional.yampolskiy.hibernate.repositories.OrderItemRepository;

public class OrderItemServiceImpl extends AbstractService<OrderItem, Long> implements OrderItemService {
    public OrderItemServiceImpl(OrderItemRepository repository) {
        super(repository);
    }
}

package ru.otus.java.professional.yampolskiy.hibernate.services;

import ru.otus.java.professional.yampolskiy.hibernate.entities.Order;
import ru.otus.java.professional.yampolskiy.hibernate.entities.OrderItem;
import ru.otus.java.professional.yampolskiy.hibernate.entities.Product;
import ru.otus.java.professional.yampolskiy.hibernate.repositories.OrderRepositoryImpl;
import ru.otus.java.professional.yampolskiy.hibernate.repositories.interfaces.OrderRepository;
import ru.otus.java.professional.yampolskiy.hibernate.services.interfaces.OrderService;

import java.util.List;
import java.util.Map;

public class OrderServiceImpl extends AbstractService<Order, Long> implements OrderService {
    private final OrderRepository orderRepository;
    public OrderServiceImpl(OrderRepositoryImpl repository) {
        super(repository);
        this.orderRepository = repository;
    }

    @Override
    public void saveOrderWithItems(Long clientId, Map<Long, Integer> productQuantities){
        orderRepository.saveOrderWithItems(clientId,  productQuantities);
    }

    @Override
    public List<Product> findProductsByOrderId(Long orderId) {
        return orderRepository.findProductsByOrderId(orderId);
    }
}

package ru.otus.java.professional.yampolskiy.hibernate.repositories.interfaces;

import ru.otus.java.professional.yampolskiy.hibernate.entities.Order;
import ru.otus.java.professional.yampolskiy.hibernate.entities.OrderItem;
import ru.otus.java.professional.yampolskiy.hibernate.entities.Product;

import java.util.List;
import java.util.Map;

public interface OrderRepository extends Repository<Order, Long> {
    void saveOrderWithItems(Long clientId, Map<Long, Integer> productQuantities);
    List<Product> findProductsByOrderId(Long orderId);
}

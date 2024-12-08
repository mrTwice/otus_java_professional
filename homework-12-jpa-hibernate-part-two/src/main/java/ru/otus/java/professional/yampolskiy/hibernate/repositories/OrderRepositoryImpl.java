package ru.otus.java.professional.yampolskiy.hibernate.repositories;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import ru.otus.java.professional.yampolskiy.hibernate.entities.Client;
import ru.otus.java.professional.yampolskiy.hibernate.entities.Order;
import ru.otus.java.professional.yampolskiy.hibernate.entities.OrderItem;
import ru.otus.java.professional.yampolskiy.hibernate.entities.Product;
import ru.otus.java.professional.yampolskiy.hibernate.exceptions.FindException;
import ru.otus.java.professional.yampolskiy.hibernate.repositories.interfaces.OrderRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class OrderRepositoryImpl extends AbstractRepository<Order, Long> implements OrderRepository {
    public OrderRepositoryImpl(SessionFactory sessionFactory) {
        super(Order.class, sessionFactory);
    }

    @Override
    public void saveOrderWithItems(Long clientId, Map<Long, Integer> productQuantities) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            Client client = session.get(Client.class, clientId);
            if (client == null) {
                throw new FindException(Client.class.getSimpleName(), "Клиент с ID " + clientId + " не найден");
            }

            Order order = new Order();
            order.setClient(client);
            order.setOrderDate(LocalDate.now());
            session.persist(order);

            for (Map.Entry<Long, Integer> entry : productQuantities.entrySet()) {
                Long productId = entry.getKey();
                Integer quantity = entry.getValue();

                Product product = session.get(Product.class, productId);
                if (product == null) {
                    throw new IllegalArgumentException("Товар с ID " + productId + " не найден");
                }

                OrderItem item = new OrderItem();
                item.setOrder(order);
                item.setProduct(product);
                item.setQuantity(quantity);
                item.setPriceAtOrdering(product.getCurrentPrice());
                session.persist(item);
            }

            transaction.commit();
        } catch (Exception e) {
            rollbackTransaction(transaction);
        }
    }


    @Override
    public List<Product> findProductsByOrderId(Long orderId) {
        try (Session session = sessionFactory.openSession()) {
            String hql = "SELECT oi.product FROM OrderItem oi WHERE oi.order.id = :orderId";
            return session.createQuery(hql, Product.class)
                    .setParameter("orderId", orderId)
                    .list();
        }
    }
}

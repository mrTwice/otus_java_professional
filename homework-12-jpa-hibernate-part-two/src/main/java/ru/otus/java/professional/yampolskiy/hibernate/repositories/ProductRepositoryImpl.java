package ru.otus.java.professional.yampolskiy.hibernate.repositories;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import ru.otus.java.professional.yampolskiy.hibernate.entities.Client;
import ru.otus.java.professional.yampolskiy.hibernate.entities.PriceHistory;
import ru.otus.java.professional.yampolskiy.hibernate.entities.Product;
import ru.otus.java.professional.yampolskiy.hibernate.repositories.interfaces.ProductRepository;

import java.util.List;

public class ProductRepositoryImpl extends AbstractRepository<Product, Long> implements ProductRepository {
    public ProductRepositoryImpl(SessionFactory sessionFactory) {
        super(Product.class, sessionFactory);
    }

    @Override
    public Product findByName(String title) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM " + Product.class.getSimpleName() + " WHERE title = :title", Product.class)
                    .setParameter("title", title)
                    .uniqueResult();
        }
    }

    @Override
    public List<PriceHistory> findPriceHistoryById(Long productId) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                            "FROM PriceHistory ph WHERE ph.product.id = :productId ORDER BY ph.createdAt DESC",
                            PriceHistory.class)
                    .setParameter("productId", productId)
                    .getResultList();
        }
    }


    @Override
    public List<Client> findClientsByProductId(Long productId) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                            "SELECT DISTINCT c " +
                                    "FROM Client c " +
                                    "JOIN c.orders o " +
                                    "JOIN o.orderItems oi " +
                                    "JOIN oi.product p " +
                                    "WHERE p.id = :productId",
                            Client.class)
                    .setParameter("productId", productId)
                    .getResultList();
        }
    }


}

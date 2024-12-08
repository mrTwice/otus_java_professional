package ru.otus.java.professional.yampolskiy.hibernate.repositories;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import ru.otus.java.professional.yampolskiy.hibernate.entities.Client;
import ru.otus.java.professional.yampolskiy.hibernate.entities.Product;
import ru.otus.java.professional.yampolskiy.hibernate.repositories.interfaces.ClientRepository;

import java.util.List;

public class ClientRepositoryImpl extends AbstractRepository<Client, Long> implements ClientRepository {
    public ClientRepositoryImpl(SessionFactory sessionFactory) {
        super(Client.class, sessionFactory);
    }

    @Override
    public Client findClientPurchasesWithDetails(Long clientId) {
        try (Session session = sessionFactory.openSession()) {
            String hql = "SELECT c FROM Client c " +
                    "JOIN FETCH c.orders o " +
                    "JOIN FETCH o.orderItems oi " +
                    "JOIN FETCH oi.product p " +
                    "WHERE c.id = :clientId";
            return session.createQuery(hql, Client.class)
                    .setParameter("clientId", clientId)
                    .getSingleResult();
        }
    }

    @Override
    public List<Product> getOrderingProductsByClientId(Long clientId) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                            "SELECT DISTINCT p " +
                                    "FROM Product p " +
                                    "JOIN OrderItem oi ON p.id = oi.product.id " +
                                    "JOIN Order o ON oi.order.id = o.id " +
                                    "JOIN Client c ON o.client.id = c.id " +
                                    "WHERE c.id = :clientId",
                            Product.class)
                    .setParameter("clientId", clientId)
                    .getResultList();
        }
    }

    @Override
    public Client findByLogin(String login) {
        try (Session session = sessionFactory.openSession()) {
            String hql = "FROM Client c WHERE c.login = :login";
            return session.createQuery(hql, Client.class)
                    .setParameter("login", login)
                    .uniqueResult();
        }
    }
}

package ru.otus.java.professional.yampolskiy.hibernate.repositories;

import org.hibernate.SessionFactory;
import ru.otus.java.professional.yampolskiy.hibernate.entities.PriceHistory;

public class PriceHistoryRepository extends AbstractRepository<Long, PriceHistory> {
    public PriceHistoryRepository(Class<Long> entityClass, SessionFactory sessionFactory) {
        super(entityClass, sessionFactory);
    }
}

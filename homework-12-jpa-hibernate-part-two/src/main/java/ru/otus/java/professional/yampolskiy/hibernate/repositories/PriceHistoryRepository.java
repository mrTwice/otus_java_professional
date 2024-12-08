package ru.otus.java.professional.yampolskiy.hibernate.repositories;

import org.hibernate.SessionFactory;
import ru.otus.java.professional.yampolskiy.hibernate.entities.PriceHistory;

public class PriceHistoryRepository extends AbstractRepository<PriceHistory, Long> {
    public PriceHistoryRepository(SessionFactory sessionFactory) {
        super(PriceHistory.class, sessionFactory);
    }
}

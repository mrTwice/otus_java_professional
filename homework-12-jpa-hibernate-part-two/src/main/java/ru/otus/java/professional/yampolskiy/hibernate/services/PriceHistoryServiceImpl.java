package ru.otus.java.professional.yampolskiy.hibernate.services;

import ru.otus.java.professional.yampolskiy.hibernate.entities.PriceHistory;
import ru.otus.java.professional.yampolskiy.hibernate.repositories.PriceHistoryRepository;
import ru.otus.java.professional.yampolskiy.hibernate.services.interfaces.PriceHistoryService;

public class PriceHistoryServiceImpl extends AbstractService<PriceHistory, Long> implements PriceHistoryService {
    public PriceHistoryServiceImpl(PriceHistoryRepository repository) {
        super(repository);
    }
}

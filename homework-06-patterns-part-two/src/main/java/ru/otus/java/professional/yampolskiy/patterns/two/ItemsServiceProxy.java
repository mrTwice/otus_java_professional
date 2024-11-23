package ru.otus.java.professional.yampolskiy.patterns.two;

import ru.otus.java.professional.yampolskiy.patterns.two.configuration.DataSource;
import ru.otus.java.professional.yampolskiy.patterns.two.service.ItemsService;
import ru.otus.java.professional.yampolskiy.patterns.two.service.Service;
import ru.otus.java.professional.yampolskiy.patterns.two.transactional.TransactionManager;
import ru.otus.java.professional.yampolskiy.patterns.two.transactional.TransactionalProxy;

public class ItemsServiceProxy{
    private static final DataSource dataSource = DataSource.getInstance();
    private static final Service itemsService = ItemsService.getInstance();

    public static Service create () {
        TransactionManager transactionManager = new TransactionManager(dataSource);
        return TransactionalProxy.createProxy(itemsService, transactionManager);
    }
}

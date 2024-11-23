package ru.otus.java.professional.yampolskiy.patterns.two.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.otus.java.professional.yampolskiy.patterns.two.repository.Repository;
import ru.otus.java.professional.yampolskiy.patterns.two.configuration.DataSource;
import ru.otus.java.professional.yampolskiy.patterns.two.model.Item;
import ru.otus.java.professional.yampolskiy.patterns.two.repository.ItemsDao;

import java.util.List;
import java.util.Random;

public class ItemsService implements Service{
    private final Logger logger = LogManager.getLogger(ItemsService.class);
    private static Service INSTANCE;
    private final Repository itemsDao;

    private ItemsService() {
        itemsDao = ItemsDao.getInstance();
    }

    public static Service getInstance() {
        if (INSTANCE == null) {
            synchronized (DataSource.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ItemsService();
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public void fillData() {
        Random random = new Random();
        for (int i = 0; i < 100; i++) {
            itemsDao.add(new Item(null, "item"+ i, random.nextDouble(0, 1000)));
        }
    }

    @Override
    public void updatePrice() {
        List<Item> items = itemsDao.getAll();

        for (Item item : items) {
            try {
                item.setPrice(item.getPrice() * 2);
                itemsDao.update(item);
                logger.info("Цена для объекта с id={} успешно обновлена.", item.getId());
            } catch (Exception e) {
                logger.error("Ошибка при обновлении цены для объекта с id={}: ", item.getId(), e);
            }
        }
    }

    public void clearDatabase() {
        itemsDao.deleteAll();
        logger.info("База данных успешно очищена.");
    }
}

package ru.otus.java.professional.yampolskiy.patterns.two;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Random;

public class ItemsService implements Service{
    private final Logger logger = LogManager.getLogger(ItemsService.class);
    private static ItemsService INSTANCE;
    private final ItemsDao itemsDao;

    private ItemsService() {
        itemsDao = ItemsDao.getInstance();
    }

    public static ItemsService getInstance() {
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
                // Увеличиваем цену в 2 раза
                item.setPrice(item.getPrice() * 2);
                // Обновляем объект в БД
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

package ru.otus.java.professional.yampolskiy.homework10springcontext.components;

import jakarta.annotation.PostConstruct;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.otus.java.professional.yampolskiy.homework10springcontext.entities.Product;

import java.util.ArrayList;
import java.util.List;

@Component
@Scope("prototype")
public class Cart {
    private static final Logger logger = LogManager.getLogger(Cart.class);
    private List<Product> items;

    @PostConstruct
    public void init() {
        items = new ArrayList<>();
    }

    public void addProduct(Product product) {
        items.add(product);
        logger.info("Товар добавлен в корзину: {}", product);
    }

    public void removeProduct(Product product) {
        items.remove(product);
        logger.info("Товар удалён из корзины: {}", product);
    }

    public List<Product> getItems() {
        return new ArrayList<>(items);
    }
}


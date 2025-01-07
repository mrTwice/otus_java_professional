package ru.otus.java.professional.yampolskiy.homework10springcontext.repositories;

import jakarta.annotation.PostConstruct;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import ru.otus.java.professional.yampolskiy.homework10springcontext.entities.Product;

import java.util.*;

@Component
public class ProductRepository {
    private static final Logger logger = LogManager.getLogger(ProductRepository.class);
    private final Map<Long, Product> products = new HashMap<>();

    @PostConstruct
    public void init() {
        for (long i = 1; i <= 10; i++) {
            products.put(i, new Product(i, "Товар " + i, i * 10.0));
        }
        logger.info("Репозиторий продуктов инициализирован с {} товарами", products.size());
    }

    public List<Product> getAllProducts() {
        return new ArrayList<>(products.values());
    }

    public Optional<Product> getProductById(Long id) {
        return Optional.ofNullable(products.get(id));
    }
}

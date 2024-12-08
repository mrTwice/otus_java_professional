package ru.otus.java.professional.yampolskiy.homework11springbootpartone.repositories;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import ru.otus.java.professional.yampolskiy.homework11springbootpartone.entities.Product;
import ru.otus.java.professional.yampolskiy.homework11springbootpartone.interfaces.ProductRepository;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class InMemoryProductRepository implements ProductRepository {

    private Map<Long, Product> products;
    private AtomicLong idGenerator;

    @PostConstruct
    public void init() {
        this.products = new ConcurrentHashMap<>();
        this.idGenerator = new AtomicLong(1);
    }

    @Override
    public Optional<Product> findById(Long id) {
        return Optional.ofNullable(products.get(id));
    }

    @Override
    public Optional<Product> save(Product product) {
        if (product.getId() == null) {
            product.setId(idGenerator.getAndIncrement());
        }
        products.put(product.getId(), product);
        return Optional.of(product);
    }

    @Override
    public Optional<Product> deleteById(Long id) {
        return Optional.ofNullable(products.remove(id));
    }

    @Override
    public List<Product> findAll() {
        return new ArrayList<>(products.values());
    }
}

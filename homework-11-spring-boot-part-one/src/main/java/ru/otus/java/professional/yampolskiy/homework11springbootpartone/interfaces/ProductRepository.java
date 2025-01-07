package ru.otus.java.professional.yampolskiy.homework11springbootpartone.interfaces;

import ru.otus.java.professional.yampolskiy.homework11springbootpartone.entities.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    Optional<Product> findById(Long id);
    Optional<Product> save(Product product);
    Optional<Product> deleteById(Long id);
    List<Product> findAll();
}

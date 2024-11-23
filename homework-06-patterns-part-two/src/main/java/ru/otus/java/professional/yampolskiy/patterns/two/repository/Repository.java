package ru.otus.java.professional.yampolskiy.patterns.two.repository;

import ru.otus.java.professional.yampolskiy.patterns.two.model.Item;

import java.util.List;
import java.util.Optional;

public interface Repository {
    Optional<Item> getById(Long id);
    List<Item> getAll();
    Optional<Item> add(Item item);
    Optional<Item> update(Item item);
    Optional<Item> delete(Long Id);
    void deleteAll();
}

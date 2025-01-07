package ru.otus.java.professional.yampolskiy.spring.data.jdbc.repositories;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;
import ru.otus.java.professional.yampolskiy.spring.data.jdbc.entities.Product;

@Repository
public interface ProductRepository extends ListCrudRepository<Product, Long> {
}

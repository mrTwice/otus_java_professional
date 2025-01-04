package ru.otus.java.professional.yampolskiy.spring.data.jdbc.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.java.professional.yampolskiy.spring.data.jdbc.entities.Product;
import ru.otus.java.professional.yampolskiy.spring.data.jdbc.exceptions.FindException;
import ru.otus.java.professional.yampolskiy.spring.data.jdbc.exceptions.SaveException;
import ru.otus.java.professional.yampolskiy.spring.data.jdbc.repositories.ProductRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Product findById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new FindException(Product.class.getSimpleName(), "Объект с ID " + id + " не найден"));
    }

    public Product save(Product product) {
        if (product.getId() != null) {
            throw new SaveException(Product.class.getSimpleName(), "Ошибка при сохранении объекта: ID NOT NULL. ID = " + product.getId());
        }
        return productRepository.save(product);
    }

    public Product update(Product product) {
        if (product.getId() == null) {
            throw new SaveException(Product.class.getSimpleName(), "Ошибка при обновлении объекта: ID IS NULL. Product: " + product);
        }
        return productRepository.save(product);
    }

    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }

}

package ru.otus.java.professional.yampolskiy.homework11springbootpartone.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.java.professional.yampolskiy.homework11springbootpartone.entities.Product;
import ru.otus.java.professional.yampolskiy.homework11springbootpartone.exceptions.*;
import ru.otus.java.professional.yampolskiy.homework11springbootpartone.interfaces.ProductRepository;
import ru.otus.java.professional.yampolskiy.homework11springbootpartone.interfaces.ProductService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
    }

    @Override
    public Product createProduct(Product product) {
        if (product.getId() != null && productRepository.findById(product.getId()).isPresent()) {
            throw new ProductAlreadyExistsException(product.getId());
        }

        return productRepository.save(product)
                .orElseThrow(() -> new ProductNotSavedException("Не удалось сохранить продукт"));
    }

    @Override
    public Product updateProduct(Product product) {
        if (product.getId() == null) {
            throw new ProductIdNotNullException();
        }

        if (productRepository.findById(product.getId()).isEmpty()) {
            throw new ProductNotFoundException(product.getId());
        }

        return productRepository.save(product)
                .orElseThrow(() -> new ProductNotUpdatedException("Не удалось обновить продукт"));
    }

    @Override
    public Product deleteProductById(Long id) {
        return productRepository.deleteById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
}

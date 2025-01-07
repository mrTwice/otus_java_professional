package ru.otus.java.professional.yampolskiy.homework11springbootpartone.interfaces;

import ru.otus.java.professional.yampolskiy.homework11springbootpartone.entities.Product;

import java.util.List;

public interface ProductService {
    Product getProductById(Long id);
    Product createProduct(Product product);
    Product updateProduct(Product product);
    Product deleteProductById(Long id);
    List<Product> getAllProducts();
}

package ru.otus.java.professional.yampolskiy.homework11springbootpartone.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.otus.java.professional.yampolskiy.homework11springbootpartone.dtos.ProductDTO;
import ru.otus.java.professional.yampolskiy.homework11springbootpartone.entities.Product;
import ru.otus.java.professional.yampolskiy.homework11springbootpartone.interfaces.ProductService;

import java.util.List;
import java.util.function.Function;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    private final Function<Product, ProductDTO> toDTO =
            product -> new ProductDTO(product.getId(), product.getTitle(), product.getPrice());

    private final Function<ProductDTO, Product> toEntity =
            productDTO -> {
                Product product = new Product();
                product.setId(productDTO.id());
                product.setTitle(productDTO.title());
                product.setPrice(productDTO.price());
                return product;
            };

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable long id) {
        Product product = productService.getProductById(id);
        return ResponseEntity.ok(toDTO.apply(product));
    }

    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        List<ProductDTO> productDTOs = products.stream()
                .map(toDTO)
                .toList();
        return ResponseEntity.ok(productDTOs);
    }

    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductDTO productDTO) {
        Product product = toEntity.apply(productDTO);
        Product createdProduct = productService.createProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(toDTO.apply(createdProduct));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable long id, @RequestBody ProductDTO productDTO) {
        Product product = toEntity.apply(productDTO);
        product.setId(id);
        Product updatedProduct = productService.updateProduct(product);
        return ResponseEntity.ok(toDTO.apply(updatedProduct));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable long id) {
        productService.deleteProductById(id);
        return ResponseEntity.noContent().build();
    }
}


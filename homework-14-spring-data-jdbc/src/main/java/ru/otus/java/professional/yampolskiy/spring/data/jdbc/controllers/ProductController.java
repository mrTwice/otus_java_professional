package ru.otus.java.professional.yampolskiy.spring.data.jdbc.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.otus.java.professional.yampolskiy.spring.data.jdbc.dtos.ProductDTO;
import ru.otus.java.professional.yampolskiy.spring.data.jdbc.entities.Product;
import ru.otus.java.professional.yampolskiy.spring.data.jdbc.services.ProductService;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequestMapping("/api/v1/products")
@RestController
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    private final Function<Product, ProductDTO> toProductDTO =
            product -> new ProductDTO(product.getId(), product.getName(), product.getPrice());

    private final Function<ProductDTO, Product> toProduct =
            productDTO -> new Product(productDTO.id(), productDTO.name(), productDTO.price());

    @GetMapping
    public List<ProductDTO> getAllProducts() {
        return productService.findAll().stream().map(toProductDTO).collect(Collectors.toList());
    }
    @GetMapping("/{id}")
    public ProductDTO getProductById(@PathVariable Long id) {
        Product product = productService.findById(id);
        return toProductDTO.apply(product);
    }

    @PostMapping
    public ProductDTO createProduct(@RequestBody ProductDTO productDTO) {
        Product product = toProduct.apply(productDTO);
        Product createdProduct = productService.save(product);
        return toProductDTO.apply(createdProduct);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteById(id);
    }

    @PutMapping
    public ProductDTO updateProduct(@RequestBody ProductDTO productDTO) {
        Product product = productService.update(toProduct.apply(productDTO));
        return toProductDTO.apply(product);
    }
}

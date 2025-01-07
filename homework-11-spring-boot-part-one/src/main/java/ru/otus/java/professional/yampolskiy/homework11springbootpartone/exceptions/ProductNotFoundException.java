package ru.otus.java.professional.yampolskiy.homework11springbootpartone.exceptions;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(Long id) {
        super(String.format("Продукт с id = %d не существует", id));
    }
}

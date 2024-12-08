package ru.otus.java.professional.yampolskiy.homework11springbootpartone.exceptions;

public class ProductAlreadyExistsException extends RuntimeException {
    public ProductAlreadyExistsException(Long id) {
        super(String.format("Продукт с id = %d уже существует", id));
    }
}

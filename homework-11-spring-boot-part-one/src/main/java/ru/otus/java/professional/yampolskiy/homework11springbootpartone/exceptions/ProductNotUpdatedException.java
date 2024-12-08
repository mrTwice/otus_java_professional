package ru.otus.java.professional.yampolskiy.homework11springbootpartone.exceptions;

public class ProductNotUpdatedException extends RuntimeException {
    public ProductNotUpdatedException(String message) {
        super(message);
    }
}

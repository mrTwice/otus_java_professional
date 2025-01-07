package ru.otus.java.professional.yampolskiy.homework11springbootpartone.exceptions;

public class ProductNotSavedException extends RuntimeException {
    public ProductNotSavedException(String message) {
        super(message);
    }
}

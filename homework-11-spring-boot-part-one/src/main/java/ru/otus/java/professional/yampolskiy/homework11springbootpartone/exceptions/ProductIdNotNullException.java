package ru.otus.java.professional.yampolskiy.homework11springbootpartone.exceptions;

public class ProductIdNotNullException extends RuntimeException {
    public ProductIdNotNullException() {
        super("ID продукта не должен быть null");
    }
}

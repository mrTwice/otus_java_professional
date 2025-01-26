package ru.otus.java.professional.yampolskiy.spring.homework15restserviceonspring.exceptions_handling.base;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}

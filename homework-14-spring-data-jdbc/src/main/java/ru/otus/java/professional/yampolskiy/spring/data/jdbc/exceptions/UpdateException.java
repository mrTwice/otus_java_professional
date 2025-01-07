package ru.otus.java.professional.yampolskiy.spring.data.jdbc.exceptions;

public class UpdateException extends ServiceException {
    public UpdateException(String entityType, String message) {
        super(entityType, message);
    }

    public UpdateException(String entityType, String message, Throwable cause) {
        super(entityType, message, cause);
    }
}
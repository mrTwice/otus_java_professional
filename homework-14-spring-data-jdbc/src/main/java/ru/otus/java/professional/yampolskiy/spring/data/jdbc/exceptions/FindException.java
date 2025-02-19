package ru.otus.java.professional.yampolskiy.spring.data.jdbc.exceptions;

public class FindException extends ServiceException {
    public FindException(String entityType, String message) {
        super(entityType, message);
    }

    public FindException(String entityType, String message, Throwable cause) {
        super(entityType, message, cause);
    }
}

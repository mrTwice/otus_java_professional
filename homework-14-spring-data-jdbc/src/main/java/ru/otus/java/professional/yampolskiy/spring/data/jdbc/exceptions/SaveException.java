package ru.otus.java.professional.yampolskiy.spring.data.jdbc.exceptions;

public class SaveException extends ServiceException {
    public SaveException(String entityType, String message) {
        super(entityType, message);
    }

    public SaveException(String entityType, String message, Throwable cause) {
        super(entityType, message, cause);
    }
}

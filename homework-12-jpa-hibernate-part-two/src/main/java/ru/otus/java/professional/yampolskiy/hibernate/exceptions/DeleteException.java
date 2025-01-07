package ru.otus.java.professional.yampolskiy.hibernate.exceptions;

public class DeleteException extends ServiceException {
    public DeleteException(String entityType, String message) {
        super(entityType, message);
    }

    public DeleteException(String entityType, String message, Throwable cause) {
        super(entityType, message, cause);
    }
}

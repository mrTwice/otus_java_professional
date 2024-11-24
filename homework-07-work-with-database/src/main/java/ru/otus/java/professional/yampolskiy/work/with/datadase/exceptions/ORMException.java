package ru.otus.java.professional.yampolskiy.work.with.datadase.exceptions;

public class ORMException extends RuntimeException {
    public ORMException(String message) {
        super(message);
    }

    public ORMException(String message, Throwable cause) {
        super(message, cause);
    }

    public ORMException(Throwable cause) {
        super(cause);
    }
}


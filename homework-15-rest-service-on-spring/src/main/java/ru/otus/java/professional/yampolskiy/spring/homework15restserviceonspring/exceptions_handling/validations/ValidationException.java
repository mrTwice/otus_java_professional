package ru.otus.java.professional.yampolskiy.spring.homework15restserviceonspring.exceptions_handling.validations;

import java.util.List;

public class ValidationException extends RuntimeException {
    private String code;
    private List<ValidationFieldError> errors;

    public String getCode() {
        return code;
    }

    public List<ValidationFieldError> getErrors() {
        return errors;
    }

    public ValidationException(String code, String message, List<ValidationFieldError> errors) {
        super(message);
        this.code = code;
        this.errors = errors;
    }
}

package ru.otus.java.professional.yampolskiy.spring.homework15restserviceonspring.exceptions_handling;

public class ValidationFieldError {
    private String field;
    private String message;

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ValidationFieldError() {
    }

    public ValidationFieldError(String field, String message) {
        this.field = field;
        this.message = message;
    }
}

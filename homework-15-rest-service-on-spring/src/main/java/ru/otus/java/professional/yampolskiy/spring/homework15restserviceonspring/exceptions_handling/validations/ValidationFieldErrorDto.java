package ru.otus.java.professional.yampolskiy.spring.homework15restserviceonspring.exceptions_handling.validations;

public class ValidationFieldErrorDto {
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

    public ValidationFieldErrorDto() {
    }

    public ValidationFieldErrorDto(String field, String message) {
        this.field = field;
        this.message = message;
    }
}

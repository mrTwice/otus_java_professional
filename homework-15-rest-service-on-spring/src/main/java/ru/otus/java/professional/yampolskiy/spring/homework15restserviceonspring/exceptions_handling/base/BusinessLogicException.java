package ru.otus.java.professional.yampolskiy.spring.homework15restserviceonspring.exceptions_handling.base;

public class BusinessLogicException extends RuntimeException {
    private String code;

    public String getCode() {
        return code;
    }

    public BusinessLogicException(String message, String code) {
        super(message);
        this.code = code;
    }
}

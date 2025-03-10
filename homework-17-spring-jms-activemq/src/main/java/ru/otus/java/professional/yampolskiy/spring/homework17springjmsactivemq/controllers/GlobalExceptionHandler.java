package ru.otus.java.professional.yampolskiy.spring.homework17springjmsactivemq.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.otus.java.professional.yampolskiy.spring.homework17springjmsactivemq.dto.ErrorDto;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ErrorDto> catchException(Exception e) {
        logger.error("Произошла непредвиденная ошибка", e);
        return new ResponseEntity<>(new ErrorDto("INTERNAL_SERVER_ERROR", e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

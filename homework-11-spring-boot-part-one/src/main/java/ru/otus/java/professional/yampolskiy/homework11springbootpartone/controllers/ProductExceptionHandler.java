package ru.otus.java.professional.yampolskiy.homework11springbootpartone.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.otus.java.professional.yampolskiy.homework11springbootpartone.exceptions.*;

@ControllerAdvice
public class ProductExceptionHandler {
    private final Logger logger = LogManager.getLogger(this.getClass());

    @ExceptionHandler(ProductAlreadyExistsException.class)
    public ResponseEntity<String> handleProductAlreadyExists(ProductAlreadyExistsException ex) {
        logger.error("Продукт уже существует", ex);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<String> handleProductNotFound(ProductNotFoundException ex) {
        logger.error("Продукт не найден", ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(ProductNotSavedException.class)
    public ResponseEntity<String> handleProductNotSaved(ProductNotSavedException ex) {
        logger.error("Продукт не был сохранен", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }

    @ExceptionHandler(ProductIdNotNullException.class)
    public ResponseEntity<String> handleProductIdNotNull(ProductIdNotNullException ex) {
        logger.error("id передаваемого продукта не NULL ", ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(ProductNotUpdatedException.class)
    public ResponseEntity<String> handleProductNotUpdated(ProductNotUpdatedException ex) {
        logger.error("Продукт не был сохранен", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }
}


package ru.otus.java.professional.yampolskiy.spring.data.jdbc.controllers;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.otus.java.professional.yampolskiy.spring.data.jdbc.dtos.EntityErrorDTO;
import ru.otus.java.professional.yampolskiy.spring.data.jdbc.exceptions.*;

import java.util.function.Function;

@ControllerAdvice
public class ProductExceptionsController {
    private final Function<ServiceException, EntityErrorDTO> exceptionMapper = e -> new EntityErrorDTO(e.getEntityType(), e.getMessage());


    @ExceptionHandler(FindException.class)
    public ResponseEntity<EntityErrorDTO> handleFindException(FindException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exceptionMapper.apply(e));
    }

    @ExceptionHandler(SaveException.class)
    public ResponseEntity<EntityErrorDTO> handleSaveException(SaveException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(exceptionMapper.apply(e));
    }

    @ExceptionHandler(UpdateException.class)
    public ResponseEntity<EntityErrorDTO> handleUpdateException(UpdateException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(exceptionMapper.apply(e));
    }
}

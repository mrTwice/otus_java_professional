package ru.otus.java.professional.yampolskiy.spring.homework15restserviceonspring.exceptions_handling;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.otus.java.professional.yampolskiy.spring.homework15restserviceonspring.exceptions_handling.base.BusinessLogicException;
import ru.otus.java.professional.yampolskiy.spring.homework15restserviceonspring.exceptions_handling.base.ErrorDto;
import ru.otus.java.professional.yampolskiy.spring.homework15restserviceonspring.exceptions_handling.base.ResourceNotFoundException;
import ru.otus.java.professional.yampolskiy.spring.homework15restserviceonspring.exceptions_handling.validations.ValidationErrorDto;
import ru.otus.java.professional.yampolskiy.spring.homework15restserviceonspring.exceptions_handling.validations.ValidationException;
import ru.otus.java.professional.yampolskiy.spring.homework15restserviceonspring.exceptions_handling.validations.ValidationFieldErrorDto;

@ControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(value = ResourceNotFoundException.class)
    public ResponseEntity<ErrorDto> catchResourceNotFoundException(ResourceNotFoundException e) {
        return new ResponseEntity<>(new ErrorDto("RESOURCE_NOT_FOUND", e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = ValidationException.class)
    public ResponseEntity<ValidationErrorDto> catchValidationException(ValidationException e) {
        return new ResponseEntity<>(
                new ValidationErrorDto(
                        e.getCode(),
                        e.getMessage(),
                        e.getErrors().stream().map(ve -> new ValidationFieldErrorDto(ve.getField(), ve.getMessage())).toList()
                ),
                HttpStatus.UNPROCESSABLE_ENTITY
        );
    }

    @ExceptionHandler(value = BusinessLogicException.class)
    public ResponseEntity<ErrorDto> catchBusinessLogicException(BusinessLogicException e) {
        return new ResponseEntity<>(new ErrorDto(e.getCode(), e.getMessage()), HttpStatus.BAD_REQUEST);
    }
}

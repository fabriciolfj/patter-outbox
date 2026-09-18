package com.github.fabriciolfj.outbox.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class RestAdvice {

    @ExceptionHandler(BusinessException.class)
    public ProblemDetail handlerBusinessException(final BusinessException businessException) {
        return businessException.getBody();
    }

    @ExceptionHandler(ValidationException.class)
    public ProblemDetail handlerValidationException(final ValidationException validationException) {
        return validationException.getBody();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handlerMethodArgumentNotValidException(final MethodArgumentNotValidException exception) {
        final List<ValidationException.FieldValidationError> errors = exception.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> new ValidationException.FieldValidationError(fieldError.getField(), fieldError.getDefaultMessage()))
                .toList();

        return new ValidationException(errors).getBody();
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ProblemDetail handlerConstraintViolationException(final ConstraintViolationException exception) {
        final List<ValidationException.FieldValidationError> errors = exception.getConstraintViolations().stream()
                .map(violation -> new ValidationException.FieldValidationError(violation.getPropertyPath().toString(), violation.getMessage()))
                .toList();

        return new ValidationException(errors).getBody();
    }
}

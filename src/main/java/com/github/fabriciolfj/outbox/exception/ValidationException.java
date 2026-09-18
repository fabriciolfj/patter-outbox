package com.github.fabriciolfj.outbox.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponse;

import java.util.List;

import static com.github.fabriciolfj.outbox.exception.ErrorEnum.VALIDATION_MESSAGE;

public class ValidationException extends RuntimeException implements ErrorResponse {

    private final ProblemDetail body;

    public ValidationException(final List<FieldValidationError> errors) {
        super(VALIDATION_MESSAGE.getMessage());
        this.body = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, VALIDATION_MESSAGE.getMessage());
        this.body.setProperty("errors", errors);
    }

    @Override
    public HttpStatusCode getStatusCode() {
        return HttpStatus.BAD_REQUEST;
    }

    @Override
    public ProblemDetail getBody() {
        return body;
    }

    public record FieldValidationError(String field, String message) {
    }
}

package com.github.fabriciolfj.outbox.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponse;

import static com.github.fabriciolfj.outbox.exception.ErrorEnum.BUSINESS_MESSAGE;

public class BusinessException extends RuntimeException implements ErrorResponse {

    private final ProblemDetail body;

    public BusinessException() {
        this(BUSINESS_MESSAGE.getMessage(), HttpStatus.UNPROCESSABLE_CONTENT);
    }

    public BusinessException(final String message, final HttpStatus status) {
        super(message);
        this.body = ProblemDetail.forStatusAndDetail(status, message);
    }

    @Override
    public HttpStatusCode getStatusCode() {
        return HttpStatus.valueOf(body.getStatus());
    }

    @Override
    public ProblemDetail getBody() {
        return body;
    }
}

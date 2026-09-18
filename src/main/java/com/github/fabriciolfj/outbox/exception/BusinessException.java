package com.github.fabriciolfj.outbox.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponse;

import static com.github.fabriciolfj.outbox.exception.ErrorEnum.BUSINESS_MESSAGE;

public class BusinessException extends RuntimeException implements ErrorResponse {

    private final ProblemDetail body;

    public BusinessException() {
        var message = BUSINESS_MESSAGE.getMessage();
        super(message);
        this.body = ProblemDetail.forStatusAndDetail(HttpStatus.UNPROCESSABLE_CONTENT, message);
    }

    @Override
    public HttpStatusCode getStatusCode() {
        return HttpStatus.UNPROCESSABLE_CONTENT;
    }

    @Override
    public ProblemDetail getBody() {
        return body;
    }
}

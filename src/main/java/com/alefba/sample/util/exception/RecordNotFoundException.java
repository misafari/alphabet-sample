package com.alefba.sample.util.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponseException;

import java.time.Instant;

public class RecordNotFoundException extends ErrorResponseException {

    public RecordNotFoundException() {
        super(HttpStatus.NOT_FOUND, asProblemDetail("record with id %d not found"), null);
    }

    public RecordNotFoundException(Long id) {
        super(HttpStatus.NOT_FOUND, asProblemDetail("record with id %d not found".formatted(id)), null);
    }

    public RecordNotFoundException(Class<?> clazz, Long id) {
        super(HttpStatus.NOT_FOUND, asProblemDetail("%s with id %d not found".formatted(clazz.getName(), id)), null);
    }

    public RecordNotFoundException(String title) {
        super(HttpStatus.NOT_FOUND, asProblemDetail(title.formatted("record with title %s not found")), null);
    }

    private static ProblemDetail asProblemDetail(String message) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, message);
        problemDetail.setTitle("record not found");
        problemDetail.setProperty("timestamp", Instant.now());
        return problemDetail;
    }
}

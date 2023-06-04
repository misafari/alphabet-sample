package com.alefba.sample.util.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;

@RestControllerAdvice
public class GlobalExceptionAdvice extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception, @Nullable HttpHeaders headers, @Nullable HttpStatusCode status, @Nullable WebRequest request) {
        var validationMessages = new HashMap<String, String>();

        exception.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();

            validationMessages.put(fieldName, errorMessage);
        });

        exception.getBody().setProperty("validation", validationMessages);

        return ResponseEntity.badRequest().body(exception.getBody());
    }

}

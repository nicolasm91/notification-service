package org.modak.notificationservice.controllers;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.modak.notificationservice.exceptions.ExceededRateException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@ControllerAdvice
public class ResponseEntityExceptionHandler {

    private static final String MESSAGE_KEY = "message";

    @ExceptionHandler(value = ExceededRateException.class)
    public ResponseEntity<?> exceededRateHandler(ExceededRateException exception) {
        return ResponseEntity.status(exception.getStatus())
                .body(Collections.singletonMap(MESSAGE_KEY, exception.getMessage()));
    }

    @ExceptionHandler(value = RuntimeException.class)
    public ResponseEntity<?> RuntimeExceptionHandler(RuntimeException exception) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Collections.singletonMap(MESSAGE_KEY, exception.getMessage()));
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<?> invalidRequestHandler(MethodArgumentNotValidException exception) {

        Map<String, String> errors = exception.getBindingResult().getAllErrors()
                .stream()
                .map(ResponseEntityExceptionHandler::buildErrorFieldEntry)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of(MESSAGE_KEY, "Invalid or missing values on request body",
                        "ErrorFields", errors));
    }

    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    public ResponseEntity<?> invalidRequestHandler(HttpMessageNotReadableException exception) {

        if (exception.getCause() instanceof InvalidFormatException cause) {
            String fieldName = cause.getPath()
                    .stream()
                    .map(JsonMappingException.Reference::getFieldName)
                    .findFirst()
                    .orElse("Unknown Field");

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap(MESSAGE_KEY, "Invalid value for field '" + fieldName + "'"));
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Collections.singletonMap(MESSAGE_KEY, exception.getMessage()));
    }

    private static Map.Entry<String, String> buildErrorFieldEntry(ObjectError objectError) {
        return Map.entry(((FieldError) objectError).getField(), Objects.requireNonNull(objectError.getDefaultMessage()));
    }
}

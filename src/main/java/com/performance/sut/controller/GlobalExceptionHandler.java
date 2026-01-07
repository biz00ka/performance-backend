package com.performance.sut.controller;

import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(DataAccessException.class)
    public ProblemDetail handleDataAccessException(DataAccessException ex) {
        Throwable rootCause = NestedExceptionUtils.getMostSpecificCause(ex);
        String detail = rootCause.getMessage();
        log.error("Database error occurred: {}", detail, ex);

        return ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR,
                "Database error: " + (detail != null ? detail : "An unexpected data error occurred"));
    }

    @ExceptionHandler(RuntimeException.class)
    public ProblemDetail handleRuntimeException(RuntimeException ex) {
        log.error("Unexpected error occurred", ex);
        String message = ex.getMessage() != null ? ex.getMessage() : "An unexpected error occurred";
        return ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, Objects.requireNonNull(message));
    }
}

package com.acc.countries.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;

/**
 * The GlobalExceptionHandler class
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    /**
     * Handles the CountryNotFoundException globally
     *
     * @param ex the CountryNotFoundException ex
     * @return the ResponseEntity
     */
    @ExceptionHandler(CountryNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleCountryNotFoundException(CountryNotFoundException ex) {
        log.error("Exception occurred", ex);
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problemDetail.setDetail("Country not found");
        return new ResponseEntity<>(problemDetail, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleGlobalException(Exception ex) {
        log.error("An unexpected error occurred", ex);
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        problemDetail.setDetail("An unexpected error occurred. Please try again later.");
        return new ResponseEntity<ProblemDetail>(problemDetail,HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

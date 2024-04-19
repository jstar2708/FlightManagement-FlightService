package com.jaideep.flightservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.ErrorResponse;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class FlightControllerExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(FlightServiceCustomException.class)
    public ResponseEntity<ErrorResponse> handleFlightServiceException(FlightServiceCustomException fe) {
        return new ResponseEntity<>(ErrorResponse.builder(fe, HttpStatus.NOT_FOUND, fe.getErrorCode()).build(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception e) {
        return new ResponseEntity<>(ErrorResponse.builder(
                e, HttpStatus.INTERNAL_SERVER_ERROR, "Server error"
        ).build(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

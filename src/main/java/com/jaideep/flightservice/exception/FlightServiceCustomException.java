package com.jaideep.flightservice.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class FlightServiceCustomException extends RuntimeException {
    private String errorCode;
    public FlightServiceCustomException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}

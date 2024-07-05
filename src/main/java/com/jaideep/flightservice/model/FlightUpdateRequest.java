package com.jaideep.flightservice.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
@Getter
@Setter
public class FlightUpdateRequest {

        private Long id;
        private String flightNumber;
        private String origin;
        private String destination;
        private LocalDate departureDate;
        private LocalDate arrivalDate;
        private double amount;
        private int totalSeats;
        private int availableSeats;
}

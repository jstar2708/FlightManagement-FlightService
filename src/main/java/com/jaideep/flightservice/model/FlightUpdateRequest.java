package com.jaideep.flightservice.model;

import java.time.LocalDate;

public class FlightUpdateRequest {
        private String flightNumber;
        private String origin;
        private String destination;
        private LocalDate departureDate;
        private LocalDate arrivalDate;
        private double amount;
        private int totalSeats;
        private int availableSeats;
}

package com.jaideep.flightservice.model;

import java.time.LocalDate;

public record FlightRequest(
        String flightNumber,
        String origin,
        String destination,
        LocalDate departureDate,
        LocalDate arrivalDate,
        int totalSeats,
        int availableSeats,
        double amount

) {}

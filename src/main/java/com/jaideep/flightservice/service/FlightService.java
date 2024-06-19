package com.jaideep.flightservice.service;

import com.jaideep.flightservice.model.FlightRequest;
import com.jaideep.flightservice.model.FlightResponse;

import java.util.List;

public interface FlightService {
    FlightResponse createFlight(FlightRequest flightRequest);

    List<FlightResponse> getAllFlights();

    FlightResponse getFlightByNumber(String flightNumber);

    void reserveSeats(String flightNumber, int seats);
}

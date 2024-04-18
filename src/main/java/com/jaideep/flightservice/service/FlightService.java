package com.jaideep.flightservice.service;

import com.jaideep.flightservice.model.FlightRequest;
import com.jaideep.flightservice.model.FlightResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface FlightService {
    FlightResponse createFlight(FlightRequest flightRequest);

    List<FlightResponse> getAllFlights();

    FlightResponse getFlightByNumber(String flightNumber);
}

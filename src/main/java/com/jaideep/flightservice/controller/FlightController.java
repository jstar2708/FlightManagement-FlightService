package com.jaideep.flightservice.controller;

import com.jaideep.flightservice.model.FlightRequest;
import com.jaideep.flightservice.model.FlightResponse;
import com.jaideep.flightservice.service.FlightService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/api/flight")
@RequiredArgsConstructor
public class FlightController {
    private final FlightService flightService;
    @PostMapping
    public ResponseEntity<FlightResponse> createFlight(@RequestBody FlightRequest flightRequest) {
        FlightResponse flightResponse = flightService.createFlight(flightRequest);
        return new ResponseEntity<>(flightResponse, HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<List<FlightResponse>> getAllFlights() {
        return ResponseEntity.ok(flightService.getAllFlights());
    }

    @GetMapping("/{number}")
    private ResponseEntity<FlightResponse> getFlightByNumber(@PathVariable("number") String flightNumber) {
        return ResponseEntity.ok(flightService.getFlightByNumber(flightNumber));
    }

}

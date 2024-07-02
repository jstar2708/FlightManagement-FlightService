package com.jaideep.flightservice.controller;

import com.jaideep.flightservice.model.FlightRequest;
import com.jaideep.flightservice.model.FlightResponse;
import com.jaideep.flightservice.service.FlightService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/api/flight")
@RequiredArgsConstructor
@Slf4j
public class FlightController {
    private final FlightService flightService;
    @PostMapping
    public ResponseEntity<FlightResponse> createFlight(@RequestBody FlightRequest flightRequest) {
        FlightResponse flightResponse = flightService.createFlight(flightRequest);
        log.info("Returning : " + flightResponse);
        return new ResponseEntity<>(flightResponse, HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<List<FlightResponse>> getAllFlights() {
        List<FlightResponse> flightResponses = flightService.getAllFlights();
        log.info("Returning : " + flightResponses);
        return ResponseEntity.ok(flightResponses);
    }

    @GetMapping("/{number}")
    public ResponseEntity<FlightResponse> getFlightByNumber(@PathVariable("number") String flightNumber) {
        FlightResponse flightResponse = flightService.getFlightByNumber(flightNumber);
        log.info("Returning : " + flightResponse);
        return ResponseEntity.ok(flightResponse);
    }

    @PutMapping("/reserveSeats/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void reserveSeats(@PathVariable("id") String flightNumber, @RequestParam int seats) {
        flightService.reserveSeats(flightNumber, seats);
    }

}

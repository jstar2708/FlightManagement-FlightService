package com.jaideep.flightservice.service.serviceimpl;


import com.jaideep.flightservice.entity.Flight;
import com.jaideep.flightservice.exception.FlightServiceCustomException;
import com.jaideep.flightservice.model.FlightRequest;
import com.jaideep.flightservice.model.FlightResponse;
import com.jaideep.flightservice.model.FlightUpdateRequest;
import com.jaideep.flightservice.repository.FlightRepository;
import com.jaideep.flightservice.service.FlightService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Log4j2
@RequiredArgsConstructor
@Service
public class FlightServiceImpl implements FlightService {

    private final FlightRepository flightRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    @Transactional
    public FlightResponse createFlight(FlightRequest flightRequest) {
        Flight flight = Flight.builder().flightNumber(flightRequest.flightNumber()).origin(flightRequest.origin()).destination(flightRequest.destination()).arrivalDate(flightRequest.arrivalDate()).totalSeats(flightRequest.totalSeats()).availableSeats(flightRequest.availableSeats()).amount(flightRequest.amount()).departureDate(flightRequest.departureDate()).build();
        flightRepository.save(flight);
        FlightResponse flightResponse = new FlightResponse();
        BeanUtils.copyProperties(flight, flightResponse);
        FlightUpdateRequest flightUpdateRequest = getFlightUpdateRequest(flight);
        String result = restTemplate.postForObject("http://localhost:8086/FLIGHT-SEARCH-SERVICE/v1/api/search/addFlight", flightUpdateRequest, String.class);
        log.info("Flight Created {} ", flightResponse.getFlightId());
        log.info(result);
        return flightResponse;
    }

    private static FlightUpdateRequest getFlightUpdateRequest(Flight flight) {
        FlightUpdateRequest flightUpdateRequest = new FlightUpdateRequest();
        flightUpdateRequest.setFlightNumber(flight.getFlightNumber());
        flightUpdateRequest.setId(flight.getFlightId());
        flightUpdateRequest.setOrigin(flight.getOrigin());
        flightUpdateRequest.setDestination(flight.getDestination());
        flightUpdateRequest.setDepartureDate(flightUpdateRequest.getDepartureDate());
        flightUpdateRequest.setAmount(flight.getAmount());
        flightUpdateRequest.setArrivalDate(flight.getArrivalDate());
        flightUpdateRequest.setTotalSeats(flight.getTotalSeats());
        flightUpdateRequest.setAvailableSeats(flight.getAvailableSeats());
        return flightUpdateRequest;
    }

    @Override
    public List<FlightResponse> getAllFlights() {
        List<Flight> flights = flightRepository.findAll();
        return flights.stream().map(this::mapToFlightResponse).toList();
    }

    @Override
    public FlightResponse getFlightByNumber(String flightNumber) {
        log.info("Get the flight for flight number : {}", flightNumber);
        Flight optionalFlight = flightRepository.findByFlightNumber(flightNumber).orElseThrow(() -> new FlightServiceCustomException("Flight with given flight number : " + flightNumber + ", not found", "FLIGHT_NOT_FOUND"));
        FlightResponse flightResponse = new FlightResponse();
        BeanUtils.copyProperties(optionalFlight, flightResponse);
        return flightResponse;
    }

    @Override
    @Transactional
    public void reserveSeats(String flightNumber, int seats) {
        log.info("Reserve seats {} for Flight number : {}", seats, flightNumber);
        Flight flight = flightRepository.findByFlightNumber(flightNumber).orElseThrow(() -> new FlightServiceCustomException("Flight with given id : " + flightNumber + " not found", "FLIGHT_NOT_FOUND"));

        if (flight.getAvailableSeats() < seats) {
            throw new FlightServiceCustomException("Flights does have sufficient seats", "INSUFFICIENT SEATS");
        }
        flight.setAvailableSeats(flight.getAvailableSeats() - seats);
        flightRepository.save(flight);
        log.info("Flight seats details updated successfully");
        FlightUpdateRequest flightUpdateRequest = new FlightUpdateRequest();
        BeanUtils.copyProperties(flight, flightUpdateRequest);
        restTemplate.put("http://localhost:8086/FLIGHT-SEARCH-SERVICE/v1/api/search/updateFlight", flightUpdateRequest);
        log.info("Flight updated in FlightSearchService");
    }

    private FlightResponse mapToFlightResponse(Flight flight) {
        FlightResponse flightResponse = new FlightResponse();
        BeanUtils.copyProperties(flight, flightResponse);
        return flightResponse;
    }
}

package com.jaideep.flightservice.service.serviceimpl;


import com.jaideep.flightservice.entity.Flight;
import com.jaideep.flightservice.exception.FlightServiceCustomException;
import com.jaideep.flightservice.model.FlightRequest;
import com.jaideep.flightservice.model.FlightResponse;
import com.jaideep.flightservice.repository.FlightRepository;
import com.jaideep.flightservice.service.FlightService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Log4j2
@RequiredArgsConstructor
@Service
public class FlightServiceImpl implements FlightService {

    private final FlightRepository flightRepository;
    @Override
    public FlightResponse createFlight(FlightRequest flightRequest) {
        Flight flight = Flight.builder()
                .flightNumber(flightRequest.flightNumber())
                .origin(flightRequest.origin())
                .destination(flightRequest.destination())
                .arrivalDate(flightRequest.arrivalDate())
                .totalSeats(flightRequest.totalSeats())
                .availableSeats(flightRequest.availableSeats())
                .amount(flightRequest.amount())
                .departureDate(flightRequest.departureDate())
                .build();
        flightRepository.save(flight);
        FlightResponse flightResponse = new FlightResponse();
        BeanUtils.copyProperties(flight, flightResponse);

        log.info("Flight Created {} ", flightResponse.getFlightId());
        return flightResponse;
    }

    @Override
    public List<FlightResponse> getAllFlights() {
        List<Flight> flights = flightRepository.findAll();
        return flights.stream()
                .map(this::mapToFlightResponse)
                .collect(Collectors.toList());
    }

    @Override
    public FlightResponse getFlightByNumber(String flightNumber) {
        log.info("Get the flight for flight number : {}", flightNumber);
        Flight optionalFlight = flightRepository.findByFlightNumber(flightNumber)
                .orElseThrow(
                        () -> new FlightServiceCustomException(
                                "Flight with given flight number : " + flightNumber + ", not found",
                                "FLIGHT_NOT_FOUND"
                                )
                );
        FlightResponse flightResponse = new FlightResponse();
        BeanUtils.copyProperties(optionalFlight, flightResponse);
        return flightResponse;
    }

    private FlightResponse mapToFlightResponse(Flight flight) {
        FlightResponse flightResponse = new FlightResponse();
        BeanUtils.copyProperties(flight, flightResponse);
        return flightResponse;
    }
}

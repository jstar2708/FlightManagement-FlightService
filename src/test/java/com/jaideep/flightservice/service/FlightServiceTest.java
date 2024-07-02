package com.jaideep.flightservice.service;

import com.jaideep.flightservice.entity.Flight;
import com.jaideep.flightservice.exception.FlightServiceCustomException;
import com.jaideep.flightservice.model.FlightRequest;
import com.jaideep.flightservice.model.FlightResponse;
import com.jaideep.flightservice.repository.FlightRepository;
import com.jaideep.flightservice.service.serviceimpl.FlightServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatException;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FlightServiceTest {

    @InjectMocks
    private FlightServiceImpl flightService;
    @Mock
    private FlightRepository flightRepository;

    private Flight flightOne;
    private Flight flightTwo;
    private String nonExistingFlightNumber;

    @BeforeEach
    void init() {
        flightOne = Flight.builder().amount(345.55d).departureDate(LocalDate.of(2024, 3, 1)).arrivalDate(LocalDate.of(2024, 3, 2)).availableSeats(200).totalSeats(500).destination("Delhi").origin("Indore").flightNumber("WEA244").build();
        flightTwo = Flight.builder().amount(345.55d).departureDate(LocalDate.of(2024, 3, 1)).arrivalDate(LocalDate.of(2024, 3, 2)).availableSeats(200).totalSeats(500).destination("Delhi").origin("Indore").flightNumber("WEA244").build();
        nonExistingFlightNumber = "BAD34";
    }

    @Test
    @DisplayName("Should save the flight to database and return a flight response")
    void createFlight() {
        when(flightRepository.save(any(Flight.class))).thenReturn(flightOne);
        FlightRequest flightRequest = new FlightRequest(flightOne.getFlightNumber(), flightOne.getOrigin(), flightOne.getDestination(), flightOne.getDepartureDate(), flightOne.getArrivalDate(), flightOne.getTotalSeats(), flightOne.getAvailableSeats(), flightOne.getAmount());
        FlightResponse flightResponse = flightService.createFlight(flightRequest);
        assertNotNull(flightResponse);
        assertThat(flightResponse.getFlightNumber()).isEqualTo(flightOne.getFlightNumber());
    }

    @Test
    @DisplayName("Should get all flights from database with size 2")
    void getAllFlights() {
        List<Flight> flights = new ArrayList<>();
        flights.add(flightOne);
        flights.add(flightTwo);

        when(flightRepository.findAll()).thenReturn(flights);

        List<FlightResponse> flightResponses = flightService.getAllFlights();
        assertNotNull(flightResponses);
        assertEquals(2, flightResponses.size());
    }

    @Test
    @DisplayName("Should return a flight with given flight number")
    void getFlightByNumber() {
        when(flightRepository.findByFlightNumber(anyString())).thenReturn(Optional.of(flightOne));

        FlightResponse flightResponse = flightService.getFlightByNumber(flightOne.getFlightNumber());
        assertNotNull(flightResponse);
        assertThat(flightResponse.getDestination()).isEqualTo(flightOne.getDestination());
    }

    @Test
    @DisplayName("Should throw an exception when an invalid flight number given")
    void getFlightByNumberThrowsException() {
        when(flightRepository.findByFlightNumber(nonExistingFlightNumber)).thenReturn(Optional.empty());

        assertThrows(FlightServiceCustomException.class, () -> flightService.getFlightByNumber(nonExistingFlightNumber));
    }

    @Test
    @DisplayName("Should reserve seats in the flight")
    void reserveSeats() {
        int seats = 10;

        when(flightRepository.findByFlightNumber(anyString())).thenReturn(Optional.of(flightOne));

        when(flightRepository.save(any())).thenReturn(flightOne);

        assertThatNoException().isThrownBy(() -> flightService.reserveSeats(flightOne.getFlightNumber(), seats));
        verify(flightRepository, times(1)).findByFlightNumber(anyString());
        verify(flightRepository, times(1)).save(any(Flight.class));
        assertEquals(190, flightOne.getAvailableSeats());
    }

    @Test
    @DisplayName("Should throw exception for non-existing flight number")
    void reserveSeatsFlightNumberException() {
        int seats = 10;
        when(flightRepository.findByFlightNumber(anyString())).thenReturn(Optional.empty());

        assertThatException().isThrownBy(() -> flightService.reserveSeats(nonExistingFlightNumber, seats)).withMessage("Flight with given id : " + nonExistingFlightNumber + " not found").isOfAnyClassIn(FlightServiceCustomException.class);
    }

    @Test
    @DisplayName("Should throw exception for insufficient available seats")
    void reserveSeatsInsufficientSeats() {
        int seats = 210;

        when(flightRepository.findByFlightNumber(anyString())).thenReturn(Optional.of(flightOne));

        assertThatException().isThrownBy(() -> flightService.reserveSeats(flightOne.getFlightNumber(), seats)).withMessage("Flights does have sufficient seats").isOfAnyClassIn(FlightServiceCustomException.class);
    }
}

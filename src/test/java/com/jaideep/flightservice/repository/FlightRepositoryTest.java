package com.jaideep.flightservice.repository;

import com.jaideep.flightservice.entity.Flight;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
class FlightRepositoryTest {
    @Autowired
    private FlightRepository flightRepository;
    private Flight flightOne;
    private Flight flightTwo;
    @BeforeEach
    void init() {
        flightOne = Flight.builder().amount(345.55d).departureDate(LocalDate.of(2024, 3, 1)).arrivalDate(LocalDate.of(2024, 3, 2)).availableSeats(200).totalSeats(500).destination("Delhi").origin("Indore").flightNumber("WEA244").build();
        flightTwo = Flight.builder().amount(345.55d).departureDate(LocalDate.of(2024, 3, 1)).arrivalDate(LocalDate.of(2024, 3, 2)).availableSeats(200).totalSeats(500).destination("Delhi").origin("Indore").flightNumber("WEA244").build();
    }

    @Test
    void save() {
        //Arrange - setting up the data
        //Flight flight = Flight.builder().amount(345.55d).departureDate(LocalDate.of(2024, 3, 1)).arrivalDate(LocalDate.of(2024, 3, 2)).availableSeats(200).totalSeats(500).destination("Delhi").origin("Indore").flightNumber("WEA244").build();
        //Act - calling of the method
        Flight savedFlight = flightRepository.save(flightOne);
        //Assert - check if output is valid
        assertNotNull(savedFlight);
        assertThat(savedFlight.getFlightId()).isNotNull();
    }

    @Test
    void findAll() {
        flightRepository.save(flightOne);
        flightRepository.save(flightTwo);

        List<Flight> savedFlights = flightRepository.findAll();
        assertThat(savedFlights).isNotNull();
        assertEquals(2, savedFlights.size());
    }

    @Test
    void findById() {
        Flight savedFlight = flightRepository.save(flightOne);

        Flight retrieveFlight = flightRepository.findById(savedFlight.getFlightId()).get();

        assertNotNull(retrieveFlight);
        assertEquals("Delhi", retrieveFlight.getDestination());
        assertEquals("WEA244", retrieveFlight.getFlightNumber());
    }

    @Test
    void updateFlight() {
        Flight savedFlight = flightRepository.save(flightOne);
        savedFlight.setAvailableSeats(150);
        Flight updatedFlight = flightRepository.save(savedFlight);

        assertNotNull(updatedFlight);
        assertEquals(150, updatedFlight.getAvailableSeats());
        assertEquals(500, updatedFlight.getTotalSeats());
    }

    @Test
    void delete() {
        flightRepository.save(flightOne);
        flightRepository.save(flightTwo);
        Long id = flightOne.getFlightId();

        flightRepository.delete(flightOne);

        Optional<Flight> flight = flightRepository.findById(id);
        List<Flight> totalFlights = flightRepository.findAll();
        assertEquals(1, totalFlights.size());
        assertThat(flight).isEmpty();
    }

    @Test
    void findByFlightNumber() {
        flightRepository.save(flightOne);
        Optional<Flight> retrieveFlight = flightRepository.findByFlightNumber(flightOne.getFlightNumber());
        assertThat(retrieveFlight).isNotEmpty();
        assertEquals(345.55d, retrieveFlight.get().getAmount());
    }
}

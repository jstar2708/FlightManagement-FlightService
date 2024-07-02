package com.jaideep.flightservice.repository;

import com.jaideep.flightservice.entity.Flight;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
    @DisplayName("Save the flight into the database")
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
    @DisplayName("Returns all saved flights")
    void findAll() {
        flightRepository.save(flightOne);
        flightRepository.save(flightTwo);

        List<Flight> savedFlights = flightRepository.findAll();
        assertThat(savedFlights).isNotNull();
        assertEquals(2, savedFlights.size());
    }

    @Test
    @DisplayName("Returns the Flight with the given id")
    void findById() {
        Flight savedFlight = flightRepository.save(flightOne);

        Flight retrieveFlight = flightRepository.findById(savedFlight.getFlightId()).get();

        assertNotNull(retrieveFlight);
        assertEquals("Delhi", retrieveFlight.getDestination());
        assertEquals("WEA244", retrieveFlight.getFlightNumber());
    }

    @Test
    @DisplayName("Updates a flight and saves it to the Database")
    void updateFlight() {
        Flight savedFlight = flightRepository.save(flightOne);
        savedFlight.setAvailableSeats(150);
        Flight updatedFlight = flightRepository.save(savedFlight);

        assertNotNull(updatedFlight);
        assertEquals(150, updatedFlight.getAvailableSeats());
        assertEquals(500, updatedFlight.getTotalSeats());
    }

    @Test
    @DisplayName("Deletes a flight from database")
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
    @DisplayName("Returns a flight with the given flight number")
    void findByFlightNumber() {
        flightRepository.save(flightOne);
        Optional<Flight> retrieveFlight = flightRepository.findByFlightNumber(flightOne.getFlightNumber());
        assertThat(retrieveFlight).isNotEmpty();
        assertEquals(345.55d, retrieveFlight.get().getAmount());
    }
}

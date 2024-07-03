package com.jaideep.flightservice.integration;

import com.jaideep.flightservice.entity.Flight;
import com.jaideep.flightservice.model.FlightRequest;
import com.jaideep.flightservice.model.FlightResponse;
import com.jaideep.flightservice.repository.FlightRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FlightIntegrationTest {
    private static RestTemplate restTemplate;
    @LocalServerPort
    private int port;
    private String baseUrl = "http://localhost";
    @Autowired
    private FlightRepository flightRepository;

    private Flight flightOne;
    private Flight flightTwo;

    @BeforeAll
    static void init() {
        restTemplate = new RestTemplate();
    }

    @BeforeEach
    void beforeSetUp() {
        baseUrl = baseUrl + ":" + port + "/v1/api/flight";
        flightOne = Flight.builder().amount(345.55d).departureDate(LocalDate.of(2024, 3, 1)).arrivalDate(LocalDate.of(2024, 3, 2)).availableSeats(200).totalSeats(500).destination("Delhi").origin("Indore").flightNumber("WEA244").build();
        flightTwo = Flight.builder().amount(345.55d).departureDate(LocalDate.of(2024, 3, 1)).arrivalDate(LocalDate.of(2024, 3, 2)).availableSeats(200).totalSeats(500).destination("Delhi").origin("Indore").flightNumber("WEA244").build();
    }

    @AfterEach
    void afterEachSetup() {
        flightRepository.deleteAll();
    }

    @Test
    void shouldCreateFlightTest() {
        FlightRequest flightRequest = new FlightRequest("BA456", "Vadodara", "Indore", LocalDate.of(2024, 2, 2), LocalDate.of(2024, 2, 3), 500, 230, 450.00d);
        FlightResponse flightResponse = restTemplate.postForObject(baseUrl, flightRequest, FlightResponse.class);
        assertNotNull(flightResponse);
        assertThat(flightResponse.getFlightId()).isNotNull();
    }

    @Test
    void shouldGetAllFlights() {
        flightRepository.save(flightOne);
        flightRepository.save(flightTwo);
        List<FlightResponse> flightResponses = restTemplate.getForObject(baseUrl + "/all", List.class);
        assertNotNull(flightResponses);
        assertThat(flightResponses).hasSize(2);
    }

    @Test
    void shouldGetFlightByNumber() {
        flightRepository.save(flightOne);
        FlightResponse newFlightResponse = restTemplate.getForObject(baseUrl + "/" + flightOne.getFlightNumber(), FlightResponse.class);
        assertNotNull(newFlightResponse);
        assertEquals(flightOne.getAmount(), newFlightResponse.getAmount());
    }

    @Test
    void shouldReserveSeats() {
        flightRepository.save(flightOne);
        int seats = 6;
        restTemplate.put(UriComponentsBuilder.fromHttpUrl(baseUrl + "/reserveSeats/" + flightOne.getFlightNumber()).queryParam("seats", String.valueOf(seats)).build().toUri(), null);
        FlightResponse updatedFlightResponse = restTemplate.getForObject(baseUrl + "/" + flightOne.getFlightNumber(), FlightResponse.class);
        assertNotNull(updatedFlightResponse);
        assertThat(updatedFlightResponse.getAvailableSeats()).isEqualTo(flightOne.getAvailableSeats() - seats);
    }
}

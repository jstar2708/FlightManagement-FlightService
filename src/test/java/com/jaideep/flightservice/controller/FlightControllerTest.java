package com.jaideep.flightservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jaideep.flightservice.model.FlightRequest;
import com.jaideep.flightservice.model.FlightResponse;
import com.jaideep.flightservice.service.FlightService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class FlightControllerTest {
    @MockBean
    private FlightService flightService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    private FlightResponse flightResponseOne;
    private FlightResponse flightResponseTwo;

    @BeforeEach
    void init() {
        flightResponseOne = FlightResponse.builder().flightId(1L).flightNumber("BA456").amount(450.00d).arrivalDate(LocalDate.of(2024, 2, 3)).departureDate(LocalDate.of(2024, 2, 2)).availableSeats(230).totalSeats(500).destination("Indore").origin("Vadodara").build();
        flightResponseTwo = FlightResponse.builder().flightId(2L).flightNumber("WA456").amount(550.00d).arrivalDate(LocalDate.of(2024, 12, 3)).departureDate(LocalDate.of(2024, 12, 2)).availableSeats(244).totalSeats(540).destination("Jabalpur").origin("Mumbai").build();
    }

    @Test
    @DisplayName("Should create a new flight")
    void shouldCreateNewFlight() throws Exception {
        FlightRequest flightRequest = new FlightRequest("BA456", "Vadodara", "Indore", LocalDate.of(2024, 2, 2), LocalDate.of(2024, 2, 3), 500, 230, 450.00d);

        when(flightService.createFlight(any(FlightRequest.class))).thenReturn(flightResponseOne);

        this.mockMvc.perform(post("/v1/api/flight").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(flightRequest))).andExpect(status().isCreated()).andExpect(jsonPath("$.flightNumber", is(flightResponseOne.getFlightNumber()))).andExpect(jsonPath("$.origin", is(flightResponseOne.getOrigin())));
    }

    @Test
    @DisplayName("Should fetch all the flights")
    void shouldFetchAllFlights() throws Exception {
        List<FlightResponse> flightResponses = new ArrayList<>();
        flightResponses.add(flightResponseOne);
        flightResponses.add(flightResponseTwo);

        when(flightService.getAllFlights()).thenReturn(flightResponses);

        this.mockMvc.perform(
                get("/v1/api/flight/all")
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(flightResponses.size())));
    }

    @Test
    @DisplayName("Should fetch a flight response of given flight number")
    void shouldFetchFlightResponse() throws Exception {
        when(flightService.getFlightByNumber(anyString())).thenReturn(flightResponseOne);
        this.mockMvc.perform(get("/v1/api/flight/{number}", flightResponseOne.getFlightNumber()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.origin", is(flightResponseOne.getOrigin())));
    }

    @Test
    @DisplayName("Should reserve seats")
    void shouldReserveSeats() throws Exception {
        int seats = 2;
        doNothing().when(flightService).reserveSeats("BA456", seats);
        this.mockMvc.perform(
                put("/v1/api/flight/reserveSeats/{id}", "BA456")
                        .param("seats", String.valueOf(seats))
        )
                .andExpect(status().isNoContent());
    }

}

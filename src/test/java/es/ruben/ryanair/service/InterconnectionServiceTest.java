package es.ruben.ryanair.service;

import es.ruben.ryanair.model.Flight;
import es.ruben.ryanair.model.Interconnection;
import es.ruben.ryanair.model.Route;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@Slf4j
public class InterconnectionServiceTest {

    @Mock
    private RouteService routeService;
    @Mock
    private ScheduleService scheduleService;
    private InterconnectionService interconnectionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        interconnectionService = new InterconnectionServiceImpl(routeService, scheduleService);
    }

    @Test
    public void getInterconnections() {
        // Given
        when(routeService.getRoutes())
                .thenReturn(Flux.fromIterable(Arrays.asList(
                        Route.builder().airportFrom("DUB").airportTo("WRO").build(),
                        Route.builder().airportFrom("DUB").airportTo("MAD").build(),
                        Route.builder().airportFrom("MAD").airportTo("WRO").build(),
                        Route.builder().airportFrom("STN").airportTo("LTN").build())));
        when(scheduleService.getScheduledFlights(eq("DUB"), eq("WRO"), eq(LocalDateTime.parse("2020-08-01T07:00")), eq(LocalDateTime.parse("2020-08-03T07:00"))))
                .thenReturn(Flux.fromIterable(Collections.singleton(
                        Flight.builder()
                                .departureAirport("DUB")
                                .arrivalAirport("WRO")
                                .departureDateTime(LocalDateTime.parse("2020-08-01T10:00"))
                                .arrivalDateTime(LocalDateTime.parse("2020-08-01T12:30"))
                                .build()
                )));
        when(scheduleService.getScheduledFlights(eq("DUB"), eq("MAD"), eq(LocalDateTime.parse("2020-08-01T07:00")), eq(LocalDateTime.parse("2020-08-03T07:00"))))
                .thenReturn(Flux.fromIterable(Arrays.asList(
                        Flight.builder()
                                .departureAirport("DUB")
                                .arrivalAirport("MAD")
                                .departureDateTime(LocalDateTime.parse("2020-08-01T09:30"))
                                .arrivalDateTime(LocalDateTime.parse("2020-08-01T12:00"))
                                .build(),
                        Flight.builder()
                                .departureAirport("DUB")
                                .arrivalAirport("MAD")
                                .departureDateTime(LocalDateTime.parse("2020-08-02T10:00"))
                                .arrivalDateTime(LocalDateTime.parse("2020-08-02T12:30"))
                                .build()
                )));
        when(scheduleService.getScheduledFlights(eq("MAD"), eq("WRO"), eq(LocalDateTime.parse("2020-08-01T07:00")), eq(LocalDateTime.parse("2020-08-03T07:00"))))
                .thenReturn(Flux.fromIterable(Collections.singleton(
                        Flight.builder()
                                .departureAirport("MAD")
                                .arrivalAirport("WRO")
                                .departureDateTime(LocalDateTime.parse("2020-08-02T23:00"))
                                .arrivalDateTime(LocalDateTime.parse("2020-08-03T01:30"))
                                .build()
                )));
        String departureAirport = "DUB";
        String arrivalAirport = "WRO";
        LocalDateTime departureDateTime = LocalDateTime.parse("2020-08-01T07:00");
        LocalDateTime arrivalDateTime = LocalDateTime.parse("2020-08-03T07:00");

        // When
        List<Interconnection> result = interconnectionService.getInterconnections(departureAirport, arrivalAirport, departureDateTime, arrivalDateTime).collectList().blockOptional().orElse(null);

        // Then
        assertNotNull(result);
        log.info(result.toString());
        assertEquals(3, result.size());
    }
}

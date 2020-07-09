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
        Route route1 = new Route();
        route1.setAirportFrom("DUB");
        route1.setAirportTo("WRO");
        Route route2 = new Route();
        route2.setAirportFrom("DUB");
        route2.setAirportTo("MAD");
        Route route3 = new Route();
        route3.setAirportFrom("MAD");
        route3.setAirportTo("WRO");
        Route route4 = new Route();
        route4.setAirportFrom("STN");
        route4.setAirportTo("LTN");
        List<Route> routes = Arrays.asList(route1, route2, route3, route4);
        when(routeService.getRoutes())
                .thenReturn(Flux.fromIterable(routes));
        Flight flight1 = new Flight();
        flight1.setDepartureAirport("DUB");
        flight1.setArrivalAirport("WRO");
        flight1.setDepartureDateTime(LocalDateTime.parse("2020-08-01T10:00"));
        flight1.setArrivalDateTime(LocalDateTime.parse("2020-08-01T12:30"));
        Flight flight2 = new Flight();
        flight2.setDepartureAirport("DUB");
        flight2.setArrivalAirport("MAD");
        flight2.setDepartureDateTime(LocalDateTime.parse("2020-08-01T09:30"));
        flight2.setArrivalDateTime(LocalDateTime.parse("2020-08-01T12:00"));
        Flight flight3 = new Flight();
        flight3.setDepartureAirport("DUB");
        flight3.setArrivalAirport("MAD");
        flight3.setDepartureDateTime(LocalDateTime.parse("2020-08-02T10:00"));
        flight3.setArrivalDateTime(LocalDateTime.parse("2020-08-02T12:30"));
        Flight flight4 = new Flight();
        flight4.setDepartureAirport("MAD");
        flight4.setArrivalAirport("WRO");
        flight4.setDepartureDateTime(LocalDateTime.parse("2020-08-02T23:00"));
        flight4.setArrivalDateTime(LocalDateTime.parse("2020-08-03T01:30"));
        List<Flight> dubWroSchedules = Collections.singletonList(flight1);
        when(scheduleService.getScheduledFlights(eq("DUB"), eq("WRO"), eq(LocalDateTime.parse("2020-08-01T07:00")), eq(LocalDateTime.parse("2020-08-03T07:00"))))
                .thenReturn(Flux.fromIterable(dubWroSchedules));
        List<Flight> dubMadSchedules = Arrays.asList(flight2, flight3);
        when(scheduleService.getScheduledFlights(eq("DUB"), eq("MAD"), eq(LocalDateTime.parse("2020-08-01T07:00")), eq(LocalDateTime.parse("2020-08-03T07:00"))))
                .thenReturn(Flux.fromIterable(dubMadSchedules));
        List<Flight> madWroSchedules = Collections.singletonList(flight4);
        when(scheduleService.getScheduledFlights(eq("MAD"), eq("WRO"), eq(LocalDateTime.parse("2020-08-01T07:00")), eq(LocalDateTime.parse("2020-08-03T07:00"))))
                .thenReturn(Flux.fromIterable(madWroSchedules));
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

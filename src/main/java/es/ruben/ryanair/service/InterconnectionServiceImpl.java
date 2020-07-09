package es.ruben.ryanair.service;

import es.ruben.ryanair.model.Flight;
import es.ruben.ryanair.model.Interconnection;
import es.ruben.ryanair.model.Leg;
import es.ruben.ryanair.model.Route;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Component
public class InterconnectionServiceImpl implements InterconnectionService {

    private final RouteService routeService;
    private final ScheduleService scheduleService;

    @Override
    public Flux<Interconnection> getInterconnections(String departureAirport, String arrivalAirport, LocalDateTime departureDateTime, LocalDateTime arrivalDateTime) {
        Flux<Route> routes = routeService.getRoutes();
        Flux<Interconnection> directFlights = routes
                .filter(route -> route.getAirportFrom().equals(departureAirport) && route.getAirportTo().equals(arrivalAirport))
                .flatMap(route -> generateInterconnectionsDirectFlights(route, departureDateTime, arrivalDateTime));
        Flux<Interconnection> oneStopFlights = routes
                .filter(route -> route.getAirportFrom().equals(departureAirport) && !route.getAirportTo().equals(arrivalAirport))
                .flatMap(route -> generateInterconnectionsOneStop(route, arrivalAirport, departureDateTime, arrivalDateTime));
        return Flux.concat(directFlights, oneStopFlights);
    }

    private Flux<Interconnection> generateInterconnectionsOneStop(Route route, String arrivalAirport, LocalDateTime departureDateTime, LocalDateTime arrivalDateTime) {
        Flux<Flight> firstLegFlights = scheduleService.getScheduledFlights(route.getAirportFrom(), route.getAirportTo(), departureDateTime, arrivalDateTime);
        Flux<Flight> secondLegFlights = scheduleService.getScheduledFlights(route.getAirportTo(), arrivalAirport, departureDateTime, arrivalDateTime);
        return firstLegFlights.flatMap(firstLegFlight -> secondLegFlights
                .filter(secondLegFlight -> firstLegFlight.getArrivalDateTime().plusHours(2).isBefore(secondLegFlight.getDepartureDateTime()))
                .map(secondLegFlight -> toInterconnection(firstLegFlight, secondLegFlight)));
    }

    private Flux<Interconnection> generateInterconnectionsDirectFlights(Route route, LocalDateTime departureDateTime, LocalDateTime arrivalDateTime) {
        return scheduleService.getScheduledFlights(route.getAirportFrom(), route.getAirportTo(), departureDateTime, arrivalDateTime)
                .map(this::toInterconnection);
    }

    private Interconnection toInterconnection(Flight... flights) {
        return Interconnection.builder()
                .stops(flights.length - 1)
                .legs(Arrays.stream(flights).map(this::toLeg).collect(Collectors.toList()))
                .build();
    }

    private Leg toLeg(Flight flight) {
        return Leg.builder()
                .departureAirport(flight.getDepartureAirport())
                .arrivalAirport(flight.getArrivalAirport())
                .departureDateTime(flight.getDepartureDateTime())
                .arrivalDateTime(flight.getArrivalDateTime())
                .build();
    }
}

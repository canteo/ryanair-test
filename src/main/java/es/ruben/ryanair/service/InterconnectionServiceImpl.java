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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@RequiredArgsConstructor
@Component
public class InterconnectionServiceImpl implements InterconnectionService {

    private final RouteService routeService;
    private final ScheduleService scheduleService;

    @Override
    public List<Interconnection> getInterconnections(String departureAirport, String arrivalAirport, LocalDateTime departureDateTime, LocalDateTime arrivalDateTime) {
        Flux<Route> routes = routeService.getRoutes();
        Stream<Interconnection> directFlights = routes
                .toStream().parallel()
                .filter(route -> route.getAirportFrom().equals(departureAirport) && route.getAirportTo().equals(arrivalAirport))
                .flatMap(route -> generateInterconnectionsDirectFlights(route, departureDateTime, arrivalDateTime).stream());
        Stream<Interconnection> oneStopFlights = routes
                .toStream().parallel()
                .filter(route -> route.getAirportFrom().equals(departureAirport) && !route.getAirportTo().equals(arrivalAirport))
                .flatMap(route -> generateInterconnectionsOneStop(route, arrivalAirport, departureDateTime, arrivalDateTime).stream());
        return Stream.concat(directFlights, oneStopFlights)
                .collect(Collectors.toList());
    }

    private List<Interconnection> generateInterconnectionsOneStop(Route route, String arrivalAirport, LocalDateTime departureDateTime, LocalDateTime arrivalDateTime) {
        List<Flight> firstLegFlights = scheduleService.getScheduledFlights(route.getAirportFrom(), route.getAirportTo(), departureDateTime, arrivalDateTime);
        List<Flight> secondLegFlights = scheduleService.getScheduledFlights(route.getAirportTo(), arrivalAirport, departureDateTime, arrivalDateTime);
        if (firstLegFlights.isEmpty() || secondLegFlights.isEmpty()) {
            return Collections.emptyList();
        }
        List<Interconnection> interconnections = new ArrayList<>();
        for (Flight firstLegFlight : firstLegFlights) {
            for (Flight secondLegFlight : secondLegFlights) {
                if (firstLegFlight.getArrivalDateTime().plusHours(2).isBefore(secondLegFlight.getDepartureDateTime())) {
                    interconnections.add(mapToInterconnection(firstLegFlight, secondLegFlight));
                }
            }
        }
        return interconnections;
    }

    private List<Interconnection> generateInterconnectionsDirectFlights(Route route, LocalDateTime departureDateTime, LocalDateTime arrivalDateTime) {
        return scheduleService.getScheduledFlights(route.getAirportFrom(), route.getAirportTo(), departureDateTime, arrivalDateTime)
                .stream()
                .map(this::mapToInterconnection)
                .collect(Collectors.toList());
    }

    private Interconnection mapToInterconnection(Flight... flights) {
        Interconnection interconnection = new Interconnection();
        interconnection.setStops(flights.length - 1);
        interconnection.setLegs(Arrays.stream(flights).map(this::mapToLeg).collect(Collectors.toList()));
        return interconnection;
    }

    private Leg mapToLeg(Flight flight) {
        Leg leg = new Leg();
        leg.setDepartureAirport(flight.getDepartureAirport());
        leg.setArrivalAirport(flight.getArrivalAirport());
        leg.setDepartureDateTime(flight.getDepartureDateTime());
        leg.setArrivalDateTime(flight.getArrivalDateTime());
        return leg;
    }
}

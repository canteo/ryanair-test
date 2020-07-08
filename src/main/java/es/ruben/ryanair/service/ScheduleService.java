package es.ruben.ryanair.service;

import es.ruben.ryanair.model.Flight;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;

public interface ScheduleService {
    Flux<Flight> getScheduledFlights(String departure, String arrival, LocalDateTime departureDateTime, LocalDateTime arrivalDateTime);
}

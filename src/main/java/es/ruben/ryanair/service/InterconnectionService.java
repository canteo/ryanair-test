package es.ruben.ryanair.service;

import es.ruben.ryanair.model.Interconnection;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;

public interface InterconnectionService {
    Flux<Interconnection> getInterconnections(String departureAirport, String arrivalAirport, LocalDateTime departureDateTime, LocalDateTime arrivalDateTime);
}

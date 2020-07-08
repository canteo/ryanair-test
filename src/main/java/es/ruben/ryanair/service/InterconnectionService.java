package es.ruben.ryanair.service;

import es.ruben.ryanair.model.Interconnection;

import java.time.LocalDateTime;
import java.util.List;

public interface InterconnectionService {
    List<Interconnection> getInterconnections(String departureAirport, String arrivalAirport, LocalDateTime departureDateTime, LocalDateTime arrivalDateTime);
}

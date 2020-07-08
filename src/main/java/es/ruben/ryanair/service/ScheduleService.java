package es.ruben.ryanair.service;

import es.ruben.ryanair.model.Flight;

import java.time.LocalDateTime;
import java.util.List;

public interface ScheduleService {
    List<Flight> getScheduledFlights(String departure, String arrival, LocalDateTime departureDateTime, LocalDateTime arrivalDateTime);
}

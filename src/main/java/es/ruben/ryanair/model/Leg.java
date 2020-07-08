package es.ruben.ryanair.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Leg {
    private String departureAirport;
    private LocalDateTime departureDateTime;
    private String arrivalAirport;
    private LocalDateTime arrivalDateTime;
}

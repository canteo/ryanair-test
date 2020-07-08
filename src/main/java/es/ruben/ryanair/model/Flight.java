package es.ruben.ryanair.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Flight {
    private String number;
    private String departureAirport;
    private String arrivalAirport;
    private LocalDateTime departureDateTime;
    private LocalDateTime arrivalDateTime;
}

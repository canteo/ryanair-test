package es.ruben.ryanair.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class Flight {
    private String number;
    private String departureAirport;
    private String arrivalAirport;
    private LocalDateTime departureDateTime;
    private LocalDateTime arrivalDateTime;
}

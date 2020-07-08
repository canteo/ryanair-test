package es.ruben.ryanair.dto.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LegDto {
    private String departureAirport;
    private LocalDateTime departureDateTime;
    private String arrivalAirport;
    private LocalDateTime arrivalDateTime;
}

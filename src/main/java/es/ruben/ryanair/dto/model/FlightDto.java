package es.ruben.ryanair.dto.model;

import lombok.Data;

import java.time.LocalTime;

@Data
public class FlightDto {
    private String number;
    private LocalTime departureTime;
    private LocalTime arrivalTime;
}

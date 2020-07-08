package es.ruben.ryanair.dto.model;

import lombok.Data;

@Data
public class RouteDto {
    private String airportFrom;
    private String airportTo;
    private String connectingAirport;
    private String operator;
}

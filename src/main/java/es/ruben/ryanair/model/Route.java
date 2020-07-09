package es.ruben.ryanair.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Route {
    private String airportFrom;
    private String airportTo;
}

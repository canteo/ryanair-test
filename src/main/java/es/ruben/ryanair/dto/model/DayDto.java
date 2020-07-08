package es.ruben.ryanair.dto.model;

import lombok.Data;

import java.util.List;

@Data
public class DayDto {
    private Integer day;
    private List<FlightDto> flights;
}

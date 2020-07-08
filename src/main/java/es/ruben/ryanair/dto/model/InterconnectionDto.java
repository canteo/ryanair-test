package es.ruben.ryanair.dto.model;

import lombok.Data;

import java.util.List;

@Data
public class InterconnectionDto {
    private Integer stops;
    private List<LegDto> legs;
}

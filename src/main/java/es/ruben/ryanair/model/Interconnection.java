package es.ruben.ryanair.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Interconnection {
    private Integer stops;
    private List<Leg> legs;
}

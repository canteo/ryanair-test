package es.ruben.ryanair.model;

import lombok.Data;

import java.util.List;

@Data
public class Interconnection {
    private Integer stops;
    private List<Leg> legs;
}

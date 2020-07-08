package es.ruben.ryanair.dto.model;

import lombok.Data;

import java.util.List;

@Data
public class ScheduleDto {
    private Integer month;
    private List<DayDto> days;
}

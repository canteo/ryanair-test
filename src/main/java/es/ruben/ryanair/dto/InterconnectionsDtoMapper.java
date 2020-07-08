package es.ruben.ryanair.dto;


import es.ruben.ryanair.dto.model.InterconnectionDto;
import es.ruben.ryanair.model.Interconnection;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface InterconnectionsDtoMapper {
    List<InterconnectionDto> toDto(List<Interconnection> list);

    InterconnectionDto toDto(Interconnection entity);
}

package es.ruben.ryanair.dto;


import es.ruben.ryanair.dto.model.RouteDto;
import es.ruben.ryanair.model.Route;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RouteDtoMapper {
    Route fromDto(RouteDto dto);
}

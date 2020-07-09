package es.ruben.ryanair.dto;


import es.ruben.ryanair.dto.model.ExceptionDto;
import es.ruben.ryanair.exception.RyanairTestException;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ExceptionDtoMapper {
    ExceptionDto toDto(RyanairTestException entity);
}

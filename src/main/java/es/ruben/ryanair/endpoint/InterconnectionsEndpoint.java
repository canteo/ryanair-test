package es.ruben.ryanair.endpoint;

import es.ruben.ryanair.dto.InterconnectionDtoMapper;
import es.ruben.ryanair.dto.model.InterconnectionDto;
import es.ruben.ryanair.dto.model.SearchCriteriaDto;
import es.ruben.ryanair.model.Interconnection;
import es.ruben.ryanair.service.InterconnectionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class InterconnectionsEndpoint {

    private final InterconnectionService interconnectionService;
    private final InterconnectionDtoMapper interconnectionDtoMapper;

    @GetMapping("/blocking/interconnections")
    public ResponseEntity<List<InterconnectionDto>> getInterconnectedFlights(SearchCriteriaDto searchCriteriaDto) {
        log.info("Getting all interconnections for: {}", searchCriteriaDto.toString());
        Flux<Interconnection> interconnections = interconnectionService.getInterconnections(searchCriteriaDto.getDeparture(), searchCriteriaDto.getArrival(), searchCriteriaDto.getDepartureDateTime(), searchCriteriaDto.getArrivalDateTime());
        return ResponseEntity.ok(interconnectionDtoMapper.toDto(interconnections.collectList().block()));
    }
}

package es.ruben.ryanair.reactive;

import es.ruben.ryanair.dto.InterconnectionsDtoMapper;
import es.ruben.ryanair.dto.model.InterconnectionDto;
import es.ruben.ryanair.model.Interconnection;
import es.ruben.ryanair.service.InterconnectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Objects;

@RequiredArgsConstructor
@Component
public class InterconnectionsHandler {

    private final InterconnectionService interconnectionService;
    private final InterconnectionsDtoMapper interconnectionsDtoMapper;

    public Mono<ServerResponse> interconnections(ServerRequest request) {
        String departure = Objects.requireNonNull(request.queryParam("departure").orElse(null));
        String arrival = Objects.requireNonNull(request.queryParam("arrival").orElse(null));
        LocalDateTime departureDateTime = LocalDateTime.parse(Objects.requireNonNull(request.queryParam("departureDateTime").orElse(null)));
        LocalDateTime arrivalDateTime = LocalDateTime.parse(Objects.requireNonNull(request.queryParam("arrivalDateTime").orElse(null)));
        Flux<Interconnection> interconnections = interconnectionService.getInterconnections(departure, arrival, departureDateTime, arrivalDateTime);
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromProducer(interconnections.map(interconnectionsDtoMapper::toDto), InterconnectionDto.class));
    }
}
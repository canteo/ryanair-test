package es.ruben.ryanair.reactive;

import es.ruben.ryanair.dto.ExceptionDtoMapper;
import es.ruben.ryanair.dto.InterconnectionDtoMapper;
import es.ruben.ryanair.dto.model.InterconnectionDto;
import es.ruben.ryanair.exception.QueryParamIsNullException;
import es.ruben.ryanair.exception.RyanairTestException;
import es.ruben.ryanair.model.Interconnection;
import es.ruben.ryanair.service.InterconnectionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Component
public class InterconnectionsHandler {

    private final InterconnectionService interconnectionService;
    private final InterconnectionDtoMapper interconnectionDtoMapper;
    private final ExceptionDtoMapper exceptionDtoMapper;

    public Mono<ServerResponse> interconnections(ServerRequest request) {
        try {
            String departure = getRequestParam(request, "departure");
            String arrival = getRequestParam(request, "arrival");
            LocalDateTime departureDateTime = LocalDateTime.parse(getRequestParam(request, "departureDateTime"));
            LocalDateTime arrivalDateTime = LocalDateTime.parse(getRequestParam(request, "arrivalDateTime"));
            Flux<Interconnection> interconnections = interconnectionService.getInterconnections(departure, arrival, departureDateTime, arrivalDateTime);
            return ServerResponse.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromProducer(interconnections.map(interconnectionDtoMapper::toDto), InterconnectionDto.class));
        } catch (RyanairTestException e) {
            log.error(e.getMessage(), e);
            return ServerResponse.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue(exceptionDtoMapper.toDto(e)));
        }
    }

    private String getRequestParam(ServerRequest request, String paramName) {
        return request.queryParam(paramName).orElseThrow(() -> new QueryParamIsNullException(paramName));
    }
}

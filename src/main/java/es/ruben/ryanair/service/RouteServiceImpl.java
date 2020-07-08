package es.ruben.ryanair.service;

import es.ruben.ryanair.dto.RouteDtoMapper;
import es.ruben.ryanair.dto.model.RouteDto;
import es.ruben.ryanair.model.Route;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Slf4j
@RequiredArgsConstructor
@Component
public class RouteServiceImpl implements RouteService {

    private final WebClient ryanairWebClient;
    private final RouteDtoMapper routeDtoMapper;

    @Override
    public Flux<Route> getRoutes() {
        return ryanairWebClient
                .get()
                .uri("/locate/3/routes")
                .retrieve()
                .bodyToFlux(RouteDto.class)
                .filter(routeDto -> routeDto.getConnectingAirport() == null && "RYANAIR".equals(routeDto.getOperator()))
                .map(routeDtoMapper::fromDto);
    }
}

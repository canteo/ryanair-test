package es.ruben.ryanair.service;

import es.ruben.ryanair.model.Route;
import reactor.core.publisher.Flux;

public interface RouteService {
    Flux<Route> getRoutes();
}

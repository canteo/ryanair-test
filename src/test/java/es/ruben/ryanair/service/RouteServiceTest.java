package es.ruben.ryanair.service;

import es.ruben.ryanair.dto.RouteDtoMapperImpl;
import es.ruben.ryanair.model.Route;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
public class RouteServiceTest {

    private RouteService routeService;
    private WebClient webClient;

    @Test
    public void getRoutes1() {
        // Given
        webClient = WebClient.builder()
                .baseUrl("")
                .exchangeFunction(clientRequest -> Mono.just(ClientResponse.create(HttpStatus.OK)
                        .header("content-type", "application/json")
                        .body("[{\"airportFrom\":\"AAL\",\"airportTo\":\"STN\",\"connectingAirport\":null,\"newRoute\":false,\"seasonalRoute\":false,\"operator\":\"RYANAIR\",\"group\":\"CITY\",\"similarArrivalAirportCodes\":[],\"tags\":[],\"carrierCode\":\"FR\"},{\"airportFrom\":\"AAR\",\"airportTo\":\"GDN\",\"connectingAirport\":null,\"newRoute\":false,\"seasonalRoute\":false,\"operator\":\"RYANAIR\",\"group\":\"ETHNIC\",\"similarArrivalAirportCodes\":[],\"tags\":[],\"carrierCode\":\"FR\"},{\"airportFrom\":\"AAR\",\"airportTo\":\"STN\",\"connectingAirport\":null,\"newRoute\":false,\"seasonalRoute\":false,\"operator\":\"RYANAIR\",\"group\":\"CITY\",\"similarArrivalAirportCodes\":[],\"tags\":[],\"carrierCode\":\"FR\"},{\"airportFrom\":\"AAR\",\"airportTo\":\"ZAD\",\"connectingAirport\":null,\"newRoute\":false,\"seasonalRoute\":false,\"operator\":\"RYANAIR\",\"group\":\"GENERIC\",\"similarArrivalAirportCodes\":[],\"tags\":[],\"carrierCode\":\"FR\"},{\"airportFrom\":\"ABZ\",\"airportTo\":\"AGP\",\"connectingAirport\":null,\"newRoute\":false,\"seasonalRoute\":false,\"operator\":\"RYANAIR\",\"group\":\"LEISURE\",\"similarArrivalAirportCodes\":[],\"tags\":[],\"carrierCode\":\"FR\"},{\"airportFrom\":\"ABZ\",\"airportTo\":\"ALC\",\"connectingAirport\":null,\"newRoute\":false,\"seasonalRoute\":false,\"operator\":\"RYANAIR\",\"group\":\"LEISURE\",\"similarArrivalAirportCodes\":[],\"tags\":[],\"carrierCode\":\"FR\"},{\"airportFrom\":\"ABZ\",\"airportTo\":\"FAO\",\"connectingAirport\":null,\"newRoute\":false,\"seasonalRoute\":false,\"operator\":\"RYANAIR\",\"group\":\"LEISURE\",\"similarArrivalAirportCodes\":[],\"tags\":[],\"carrierCode\":\"FR\"},{\"airportFrom\":\"ABZ\",\"airportTo\":\"MLA\",\"connectingAirport\":null,\"newRoute\":false,\"seasonalRoute\":false,\"operator\":\"RYANAIR\",\"group\":\"LEISURE\",\"similarArrivalAirportCodes\":[],\"tags\":[],\"carrierCode\":\"FR\"},{\"airportFrom\":\"ACE\",\"airportTo\":\"ATH\",\"connectingAirport\":\"BGY\",\"newRoute\":false,\"seasonalRoute\":false,\"operator\":\"RYANAIR\",\"group\":\"GENERIC\",\"similarArrivalAirportCodes\":[],\"tags\":[],\"carrierCode\":\"FR\"},{\"airportFrom\":\"ACE\",\"airportTo\":\"BDS\",\"connectingAirport\":\"BGY\",\"newRoute\":false,\"seasonalRoute\":false,\"operator\":\"RYANAIR\",\"group\":\"GENERIC\",\"similarArrivalAirportCodes\":[],\"tags\":[],\"carrierCode\":\"FR\"}]")
                        .build()))
                .build();
        routeService = new RouteServiceImpl(webClient, new RouteDtoMapperImpl());

        // When
        List<Route> result = routeService.getRoutes().collectList().blockOptional().orElse(null);

        // Then
        assertNotNull(result);
        assertEquals(8, result.size());
    }

    @Test
    public void getRoutes2() {
        // Given
        webClient = WebClient.builder()
                .baseUrl("")
                .exchangeFunction(clientRequest -> Mono.just(ClientResponse.create(HttpStatus.OK)
                        .header("content-type", "application/json")
                        .body("[{\"airportFrom\":\"AAL\",\"airportTo\":\"STN\",\"connectingAirport\":null,\"newRoute\":false,\"seasonalRoute\":false,\"operator\":\"RYANAIR\",\"group\":\"CITY\",\"similarArrivalAirportCodes\":[],\"tags\":[],\"carrierCode\":\"FR\"}]")
                        .build()))
                .build();
        routeService = new RouteServiceImpl(webClient, new RouteDtoMapperImpl());

        // When
        List<Route> result = routeService.getRoutes().collectList().blockOptional().orElse(null);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
    }
}

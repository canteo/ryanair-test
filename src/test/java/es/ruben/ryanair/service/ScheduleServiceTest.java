package es.ruben.ryanair.service;

import es.ruben.ryanair.model.Flight;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
public class ScheduleServiceTest {

    private WebClient webClient;
    private ScheduleService scheduleService;

    @Test
    public void getSchedules1() {
        // Given
        String departureAirport = "DUB";
        String arrivalAirport = "WRO";
        LocalDateTime departureDateTime = LocalDateTime.parse("2020-08-01T07:00");
        LocalDateTime arrivalDateTime = LocalDateTime.parse("2020-08-30T07:00");
        webClient = WebClient.builder()
                .baseUrl("")
                .exchangeFunction(clientRequest -> Mono.just(ClientResponse.create(HttpStatus.OK)
                        .header("content-type", "application/json")
                        .body("{\"month\":8,\"days\":[{\"day\":2,\"flights\":[{\"carrierCode\":\"FR\",\"number\":\"1926\",\"departureTime\":\"06:50\",\"arrivalTime\":\"10:25\"}]},{\"day\":3,\"flights\":[{\"carrierCode\":\"FR\",\"number\":\"1926\",\"departureTime\":\"09:45\",\"arrivalTime\":\"13:20\"}]},{\"day\":7,\"flights\":[{\"carrierCode\":\"FR\",\"number\":\"1926\",\"departureTime\":\"19:40\",\"arrivalTime\":\"23:15\"}]},{\"day\":9,\"flights\":[{\"carrierCode\":\"FR\",\"number\":\"1926\",\"departureTime\":\"06:50\",\"arrivalTime\":\"10:25\"}]},{\"day\":10,\"flights\":[{\"carrierCode\":\"FR\",\"number\":\"1926\",\"departureTime\":\"09:45\",\"arrivalTime\":\"13:20\"}]},{\"day\":14,\"flights\":[{\"carrierCode\":\"FR\",\"number\":\"1926\",\"departureTime\":\"19:40\",\"arrivalTime\":\"23:15\"}]},{\"day\":16,\"flights\":[{\"carrierCode\":\"FR\",\"number\":\"1926\",\"departureTime\":\"06:50\",\"arrivalTime\":\"10:25\"}]},{\"day\":17,\"flights\":[{\"carrierCode\":\"FR\",\"number\":\"1926\",\"departureTime\":\"09:45\",\"arrivalTime\":\"13:20\"}]},{\"day\":21,\"flights\":[{\"carrierCode\":\"FR\",\"number\":\"1926\",\"departureTime\":\"19:40\",\"arrivalTime\":\"23:15\"}]},{\"day\":23,\"flights\":[{\"carrierCode\":\"FR\",\"number\":\"1926\",\"departureTime\":\"06:50\",\"arrivalTime\":\"10:25\"}]},{\"day\":24,\"flights\":[{\"carrierCode\":\"FR\",\"number\":\"1926\",\"departureTime\":\"09:45\",\"arrivalTime\":\"13:20\"}]},{\"day\":28,\"flights\":[{\"carrierCode\":\"FR\",\"number\":\"1926\",\"departureTime\":\"19:40\",\"arrivalTime\":\"23:15\"}]},{\"day\":30,\"flights\":[{\"carrierCode\":\"FR\",\"number\":\"1926\",\"departureTime\":\"06:50\",\"arrivalTime\":\"10:25\"}]},{\"day\":31,\"flights\":[{\"carrierCode\":\"FR\",\"number\":\"1926\",\"departureTime\":\"09:45\",\"arrivalTime\":\"13:20\"}]}]}")
                        .build()))
                .build();
        scheduleService = new ScheduleServiceImpl(webClient);

        // When
        List<Flight> result = scheduleService.getScheduledFlights(departureAirport, arrivalAirport, departureDateTime, arrivalDateTime).collectList().blockOptional().orElse(null);

        // Then
        assertNotNull(result);
        assertEquals(12, result.size());
    }

    @Test
    public void getSchedules2() {
        // Given
        String departureAirport = "DUB";
        String arrivalAirport = "WRO";
        LocalDateTime departureDateTime = LocalDateTime.parse("2020-09-01T07:00");
        LocalDateTime arrivalDateTime = LocalDateTime.parse("2020-09-30T07:00");
        webClient = WebClient.builder()
                .baseUrl("")
                .exchangeFunction(clientRequest -> Mono.just(ClientResponse.create(HttpStatus.OK)
                        .header("content-type", "application/json")
                        .body("{\"month\":9,\"days\":[{\"day\":2,\"flights\":[{\"carrierCode\":\"FR\",\"number\":\"1926\",\"departureTime\":\"09:35\",\"arrivalTime\":\"13:10\"}]},{\"day\":3,\"flights\":[{\"carrierCode\":\"FR\",\"number\":\"1926\",\"departureTime\":\"19:30\",\"arrivalTime\":\"23:05\"}]},{\"day\":4,\"flights\":[{\"carrierCode\":\"FR\",\"number\":\"1926\",\"departureTime\":\"19:40\",\"arrivalTime\":\"23:15\"}]},{\"day\":6,\"flights\":[{\"carrierCode\":\"FR\",\"number\":\"1926\",\"departureTime\":\"06:50\",\"arrivalTime\":\"10:25\"}]},{\"day\":7,\"flights\":[{\"carrierCode\":\"FR\",\"number\":\"1926\",\"departureTime\":\"09:45\",\"arrivalTime\":\"13:20\"}]},{\"day\":9,\"flights\":[{\"carrierCode\":\"FR\",\"number\":\"1926\",\"departureTime\":\"09:35\",\"arrivalTime\":\"13:10\"}]},{\"day\":10,\"flights\":[{\"carrierCode\":\"FR\",\"number\":\"1926\",\"departureTime\":\"19:30\",\"arrivalTime\":\"23:05\"}]},{\"day\":11,\"flights\":[{\"carrierCode\":\"FR\",\"number\":\"1926\",\"departureTime\":\"19:40\",\"arrivalTime\":\"23:15\"}]},{\"day\":13,\"flights\":[{\"carrierCode\":\"FR\",\"number\":\"1926\",\"departureTime\":\"06:50\",\"arrivalTime\":\"10:25\"}]},{\"day\":14,\"flights\":[{\"carrierCode\":\"FR\",\"number\":\"1926\",\"departureTime\":\"09:45\",\"arrivalTime\":\"13:20\"}]},{\"day\":16,\"flights\":[{\"carrierCode\":\"FR\",\"number\":\"1926\",\"departureTime\":\"09:35\",\"arrivalTime\":\"13:10\"}]},{\"day\":17,\"flights\":[{\"carrierCode\":\"FR\",\"number\":\"1926\",\"departureTime\":\"19:30\",\"arrivalTime\":\"23:05\"}]},{\"day\":18,\"flights\":[{\"carrierCode\":\"FR\",\"number\":\"1926\",\"departureTime\":\"19:40\",\"arrivalTime\":\"23:15\"}]},{\"day\":20,\"flights\":[{\"carrierCode\":\"FR\",\"number\":\"1926\",\"departureTime\":\"06:50\",\"arrivalTime\":\"10:25\"}]},{\"day\":21,\"flights\":[{\"carrierCode\":\"FR\",\"number\":\"1926\",\"departureTime\":\"09:45\",\"arrivalTime\":\"13:20\"}]},{\"day\":23,\"flights\":[{\"carrierCode\":\"FR\",\"number\":\"1926\",\"departureTime\":\"09:35\",\"arrivalTime\":\"13:10\"}]},{\"day\":24,\"flights\":[{\"carrierCode\":\"FR\",\"number\":\"1926\",\"departureTime\":\"19:30\",\"arrivalTime\":\"23:05\"}]},{\"day\":25,\"flights\":[{\"carrierCode\":\"FR\",\"number\":\"1926\",\"departureTime\":\"19:40\",\"arrivalTime\":\"23:15\"}]},{\"day\":27,\"flights\":[{\"carrierCode\":\"FR\",\"number\":\"1926\",\"departureTime\":\"06:50\",\"arrivalTime\":\"10:25\"}]},{\"day\":28,\"flights\":[{\"carrierCode\":\"FR\",\"number\":\"1926\",\"departureTime\":\"09:45\",\"arrivalTime\":\"13:20\"}]},{\"day\":30,\"flights\":[{\"carrierCode\":\"FR\",\"number\":\"1926\",\"departureTime\":\"09:35\",\"arrivalTime\":\"13:10\"}]}]}")
                        .build()))
                .build();
        scheduleService = new ScheduleServiceImpl(webClient);

        // When
        List<Flight> result = scheduleService.getScheduledFlights(departureAirport, arrivalAirport, departureDateTime, arrivalDateTime).collectList().blockOptional().orElse(null);

        // Then
        assertNotNull(result);
        assertEquals(20, result.size());
    }
}

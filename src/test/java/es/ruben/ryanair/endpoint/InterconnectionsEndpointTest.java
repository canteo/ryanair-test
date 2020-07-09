package es.ruben.ryanair.endpoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.ruben.ryanair.dto.model.ExceptionDto;
import es.ruben.ryanair.dto.model.InterconnectionDto;
import es.ruben.ryanair.exception.QueryParamIsNullException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Disabled("these tests are only valid if the blocking version is used with Tomcat/Jetty servers")
@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class InterconnectionsEndpointTest {

    @LocalServerPort
    private int localPort;

    @Test
    public void getInterconnections() {
        // Given
        String departure = "DUB";
        String arrival = "WRO";
        LocalDateTime departureDateTime = LocalDateTime.parse("2020-08-01T07:00");
        LocalDateTime arrivalDateTime = LocalDateTime.parse("2020-08-03T07:00");

        // When
        List<InterconnectionDto> result = WebClient.create()
                .get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("http")
                        .host("localhost")
                        .port(localPort)
                        .path("/blocking/interconnections")
                        .queryParam("departure", departure)
                        .queryParam("arrival", arrival)
                        .queryParam("departureDateTime", departureDateTime.toString())
                        .queryParam("arrivalDateTime", arrivalDateTime.toString())
                        .build())
                .retrieve()
                .bodyToFlux(InterconnectionDto.class)
                .collectList()
                .blockOptional()
                .orElse(null);

        // Then
        assertNotNull(result);
        log.info(result.toString());
        assertEquals(24, result.size());
    }

    @Test
    public void getInterconnectionsThrowsErrorIfDepartureIsNull() throws IOException {
        // Given
        String arrival = "WRO";
        LocalDateTime departureDateTime = LocalDateTime.parse("2020-08-01T07:00");
        LocalDateTime arrivalDateTime = LocalDateTime.parse("2020-08-03T07:00");

        // When
        try {
            WebClient.create()
                    .get()
                    .uri(uriBuilder -> uriBuilder
                            .scheme("http")
                            .host("localhost")
                            .port(localPort)
                            .path("/blocking/interconnections")
                            .queryParam("arrival", arrival)
                            .queryParam("departureDateTime", departureDateTime.toString())
                            .queryParam("arrivalDateTime", arrivalDateTime.toString())
                            .build())
                    .retrieve()
                    .bodyToFlux(String.class)
                    .blockLast();

            // Then
            fail("It should have thrown: " + WebClientResponseException.class);
        } catch (WebClientResponseException e) {
            log.info(e.getResponseBodyAsString());
            assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
            ExceptionDto exceptionDto = new ObjectMapper().readValue(e.getResponseBodyAsString(), ExceptionDto.class);
            assertNotNull(exceptionDto);
            assertEquals(QueryParamIsNullException.class.getSimpleName(), exceptionDto.getException());
            assertEquals("'departure' query param must not be null", exceptionDto.getDescription());
        }
    }
}

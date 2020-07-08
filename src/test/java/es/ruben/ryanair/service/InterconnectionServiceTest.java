package es.ruben.ryanair.service;

import es.ruben.ryanair.model.Interconnection;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
@SpringBootTest
public class InterconnectionServiceTest {

    @Autowired
    private InterconnectionService interconnectionService;

    @Test
    public void getInterconnections() {
        // Given
        String departureAirport = "DUB";
        String arrivalAirport = "WRO";
        LocalDateTime departureDateTime = LocalDateTime.parse("2020-08-01T07:00");
        LocalDateTime arrivalDateTime = LocalDateTime.parse("2020-08-03T07:00");

        // When
        List<Interconnection> result = interconnectionService.getInterconnections(departureAirport, arrivalAirport, departureDateTime, arrivalDateTime);

        // Then
        assertNotNull(result);
        log.info(result.toString());
        assertEquals(3, result.size());
    }
}

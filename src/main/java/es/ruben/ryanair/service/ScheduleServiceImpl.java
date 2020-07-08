package es.ruben.ryanair.service;

import es.ruben.ryanair.dto.model.DayDto;
import es.ruben.ryanair.dto.model.FlightDto;
import es.ruben.ryanair.dto.model.ScheduleDto;
import es.ruben.ryanair.model.Flight;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Component
public class ScheduleServiceImpl implements ScheduleService {

    private final WebClient ryanairWebClient;

    @Override
    public List<Flight> getScheduledFlights(String departure, String arrival, LocalDateTime departureDateTime, LocalDateTime arrivalDateTime) {
        List<Flight> result = new ArrayList<>();
        for (int i = departureDateTime.getYear(); i <= arrivalDateTime.getYear(); i++) {
            for (int j = departureDateTime.getMonthValue(); j <= arrivalDateTime.getMonthValue(); j++) {
                int year = i;
                int month = j;
                List<Flight> flights = ryanairWebClient
                        .get()
                        .uri(uriBuilder -> uriBuilder
                                .path("/timtbl/3/schedules/{departure}/{arrival}/years/{year}/months/{month}")
                                .build(departure, arrival, year, month))
                        .retrieve()
                        .bodyToFlux(ScheduleDto.class)
//                        .doOnNext(dto -> log.info(dto.toString()))
                        .map(scheduleDto -> getFlights(scheduleDto, departure, arrival, departureDateTime, arrivalDateTime))
                        .blockLast();
                if (flights != null) {
                    result.addAll(flights);
                }
            }
        }
        return result;
    }

    private List<Flight> getFlights(ScheduleDto scheduleDto, String departure, String arrival, LocalDateTime departureDateTime, LocalDateTime arrivalDateTime) {
        return scheduleDto.getDays().stream()
                .flatMap(dayDto -> dayDto.getFlights().stream()
                        .map(flightDto -> getFlight(scheduleDto, dayDto, flightDto, departure, arrival, departureDateTime)))
//                .peek(flight -> log.info(flight.toString()))
                .filter(flight -> flight.getDepartureDateTime().isAfter(departureDateTime) && flight.getArrivalDateTime().isBefore(arrivalDateTime))
                .collect(Collectors.toList());
    }

    private Flight getFlight(ScheduleDto scheduleDto, DayDto dayDto, FlightDto flightDto, String departure, String arrival, LocalDateTime departureDateTime) {
        LocalDateTime flightDepartureDateTime = getDateTime(departureDateTime.getYear(), scheduleDto.getMonth(), dayDto.getDay(), flightDto.getDepartureTime());
        Duration duration = getFlightDuration(flightDto.getDepartureTime(), flightDto.getArrivalTime());
        LocalDateTime flightArrivalDateTime = flightDepartureDateTime.plusMinutes(duration.toMinutes());
        Flight flight = new Flight();
        flight.setNumber(flightDto.getNumber());
        flight.setDepartureAirport(departure);
        flight.setDepartureDateTime(flightDepartureDateTime);
        flight.setArrivalAirport(arrival);
        flight.setArrivalDateTime(flightArrivalDateTime);
        return flight;
    }

    private LocalDateTime getDateTime(Integer year, Integer month, Integer day, LocalTime time) {
        return LocalDateTime.of(LocalDate.of(year, month, day), time);
    }

    private Duration getFlightDuration(LocalTime departure, LocalTime arrival) {
        Duration duration = Duration.between(departure, arrival);
        if (duration.isNegative()) {
            return Duration.ofDays(1).plusMinutes(duration.toMinutes());
        } else {
            return duration;
        }
    }
}

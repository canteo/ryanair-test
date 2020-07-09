package es.ruben.ryanair.service;

import es.ruben.ryanair.dto.model.DayDto;
import es.ruben.ryanair.dto.model.FlightDto;
import es.ruben.ryanair.dto.model.ScheduleDto;
import es.ruben.ryanair.model.Flight;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Component
public class ScheduleServiceImpl implements ScheduleService {

    private final WebClient ryanairWebClient;

    @Override
    public Flux<Flight> getScheduledFlights(String departure, String arrival, LocalDateTime departureDateTime, LocalDateTime arrivalDateTime) {
        Flux<Flight> result = Flux.fromIterable(Collections.emptyList());
        for (int i = departureDateTime.getYear(); i <= arrivalDateTime.getYear(); i++) {
            for (int j = departureDateTime.getMonthValue(); j <= arrivalDateTime.getMonthValue(); j++) {
                int year = i;
                int month = j;
                Flux<Flight> flights = ryanairWebClient
                        .get()
                        .uri(uriBuilder -> uriBuilder
                                .path("/timtbl/3/schedules/{departure}/{arrival}/years/{year}/months/{month}")
                                .build(departure, arrival, year, month))
                        .retrieve()
                        .bodyToFlux(ScheduleDto.class)
//                        .doOnNext(dto -> log.info(dto.toString()))
                        .flatMap(scheduleDto -> Flux.fromIterable(getFlights(scheduleDto, departure, arrival, departureDateTime, arrivalDateTime)));
                result = Flux.concat(result, flights);
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
        return Flight.builder()
                .number(flightDto.getNumber())
                .departureAirport(departure)
                .arrivalAirport(arrival)
                .departureDateTime(flightDepartureDateTime)
                .arrivalDateTime(flightArrivalDateTime)
                .build();
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

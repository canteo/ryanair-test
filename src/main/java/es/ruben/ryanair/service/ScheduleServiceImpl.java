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
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@RequiredArgsConstructor
@Component
public class ScheduleServiceImpl implements ScheduleService {

    private final WebClient ryanairWebClient;

    @Override
    public Flux<Flight> getScheduledFlights(String departure, String arrival, LocalDateTime departureDateTime, LocalDateTime arrivalDateTime) {
        return IntStream.rangeClosed(departureDateTime.getYear(), arrivalDateTime.getYear())
                .mapToObj(year -> IntStream.rangeClosed(departureDateTime.getMonthValue(), arrivalDateTime.getMonthValue())
                        .mapToObj(month -> fetchFlights(departure, arrival, year, month, departureDateTime, arrivalDateTime))
                        .collect(Collectors.toList()))
                .flatMap(Collection::stream)
                .reduce(Flux.empty(), (acc, flight) -> Flux.concat(acc, flight));
    }

    private Flux<Flight> fetchFlights(String departure, String arrival, int year, int month, LocalDateTime departureDateTime, LocalDateTime arrivalDateTime) {
        return ryanairWebClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/timtbl/3/schedules/{departure}/{arrival}/years/{year}/months/{month}")
                        .build(departure, arrival, year, month))
                .retrieve()
                .bodyToFlux(ScheduleDto.class)
//                        .doOnNext(dto -> log.info(dto.toString()))
                .map(scheduleDto -> toListOfFlights(scheduleDto, departure, arrival, departureDateTime, arrivalDateTime))
                .flatMap(Flux::fromIterable);
    }

    private List<Flight> toListOfFlights(ScheduleDto scheduleDto, String departure, String arrival, LocalDateTime departureDateTime, LocalDateTime arrivalDateTime) {
        return scheduleDto.getDays().stream()
                .flatMap(dayDto -> dayDto.getFlights().stream()
                        .map(flightDto -> toFlight(scheduleDto, dayDto, flightDto, departure, arrival, departureDateTime)))
//                .peek(flight -> log.info(flight.toString()))
                .filter(flight -> flight.getDepartureDateTime().isAfter(departureDateTime) && flight.getArrivalDateTime().isBefore(arrivalDateTime))
                .collect(Collectors.toList());
    }

    private Flight toFlight(ScheduleDto scheduleDto, DayDto dayDto, FlightDto flightDto, String departure, String arrival, LocalDateTime departureDateTime) {
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

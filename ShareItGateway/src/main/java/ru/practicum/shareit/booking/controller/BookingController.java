package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Range;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.client.BookingClient;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.utils.UnsupportedStateException;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {

    private final BookingClient bookingClient;
    private static final String USER_ID_REQUEST_HEADER = "X-Sharer-User-Id";


    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader(USER_ID_REQUEST_HEADER) long userId,
                                         @RequestBody @Valid BookItemRequestDto requestDto) {
        log.info("Creating booking {}, userId={}", requestDto, userId);
        return bookingClient.create(userId, requestDto);
    }

    @PatchMapping("/{bookingId}")
    protected ResponseEntity<Object> update(@RequestHeader(USER_ID_REQUEST_HEADER) long ownerId,
                                            @RequestParam("approved") boolean approved,
                                            @PathVariable("bookingId") long bookingId) {
        return bookingClient.updateStatus(ownerId, approved, bookingId);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> findBooking(@RequestHeader(USER_ID_REQUEST_HEADER) long userId,
                                              @PathVariable Long bookingId) {
        log.info("Get booking {}, userId={}", bookingId, userId);
        return bookingClient.findBooking(userId, bookingId);
    }

    @GetMapping
    public ResponseEntity<Object> findAllBookingsByBookerId(@RequestHeader(USER_ID_REQUEST_HEADER) long userId,
                                                            @RequestParam(name = "state", defaultValue = "all") String stateParam,
                                                            @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                            @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        BookingState state = BookingState.from(stateParam)
                .orElseThrow(() -> new UnsupportedStateException("Unknown state: " + stateParam));
        log.info("Get booking with state {}, userId={}, from={}, size={}", stateParam, userId, from, size);
        return bookingClient.findAllBookingsByBookerId(userId, state, from, size);
    }

    @GetMapping("/owner")
    protected ResponseEntity<Object> findAllBookingsByItemOwner(
            @RequestHeader(USER_ID_REQUEST_HEADER) long ownerId,
            @RequestParam(name = "state", defaultValue = "ALL") String stateParam,
            @RequestParam(name = "from", required = false, defaultValue = "1")
            @Range(message = "Значение from должно быть больше нуля") int from,
            @RequestParam(name = "size", required = false, defaultValue = "20") @Positive int size) {
        BookingState state = BookingState.from(stateParam)
                .orElseThrow(() -> new UnsupportedStateException("Unknown state: " + stateParam));
        return bookingClient.findAllBookingsByItemOwner(ownerId, state, from, size);
    }
}
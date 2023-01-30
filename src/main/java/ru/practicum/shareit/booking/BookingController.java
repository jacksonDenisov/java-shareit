package ru.practicum.shareit.booking;

import org.hibernate.validator.constraints.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@Validated
public class BookingController {

    private final BookingService bookingService;

    private static final String USER_ID_REQUEST_HEADER = "X-Sharer-User-Id";

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }


    @PostMapping
    protected BookingDtoToUser create(@Valid @RequestBody BookingDtoFromUser bookingDtoFromUser,
                                      @RequestHeader(USER_ID_REQUEST_HEADER) long bookerId) {
        return bookingService.create(bookingDtoFromUser, bookerId);
    }

    @PatchMapping("/{bookingId}")
    protected BookingDtoToUser update(@RequestHeader(USER_ID_REQUEST_HEADER) long ownerId,
                                      @RequestParam("approved") boolean approved,
                                      @PathVariable("bookingId") long bookingId) throws AccessDeniedException {
        return bookingService.updateStatus(ownerId, approved, bookingId);
    }

    @GetMapping("/{bookingId}")
    protected BookingDtoToUser findBooking(@RequestHeader(USER_ID_REQUEST_HEADER) long userId,
                                           @PathVariable("bookingId") long bookingId) throws AccessDeniedException {
        return bookingService.findBooking(userId, bookingId);
    }

    @GetMapping
    protected List<BookingDtoToUser> findAllBookingsByBookerId(
            @RequestHeader(USER_ID_REQUEST_HEADER) long bookerID,
            @RequestParam(name = "state", defaultValue = "ALL") String state,
            @RequestParam(name = "from", required = false, defaultValue = "1")
            @Range(message = "Значение from должно быть больше нуля") int from,
            @RequestParam(name = "size", required = false, defaultValue = "20") @Positive int size) {
        Pageable pageable = PageRequest.of(from / size, size);
        return bookingService.findAllBookingsByBookerId(bookerID, state, pageable);
    }

    @GetMapping("/owner")
    protected List<BookingDtoToUser> findAllBookingsByItemOwner(
            @RequestHeader(USER_ID_REQUEST_HEADER) long ownerId,
            @RequestParam(name = "state", defaultValue = "ALL") String state,
            @RequestParam(name = "from", required = false, defaultValue = "1")
            @Range(message = "Значение from должно быть больше нуля") int from,
            @RequestParam(name = "size", required = false, defaultValue = "20") @Positive int size) {
        Pageable pageable = PageRequest.of(from / size, size);
        return bookingService.findAllBookingsByItemOwner(ownerId, state, pageable);
    }
}

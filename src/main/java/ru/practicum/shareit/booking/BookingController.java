package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
public class BookingController {

    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }


    @PostMapping
    protected BookingDtoToUser create(@Valid @RequestBody BookingDtoFromUser bookingDtoFromUser,
                                      @RequestHeader("X-Sharer-User-Id") long bookerId) {
        return bookingService.create(bookingDtoFromUser, bookerId);
    }

    @PatchMapping("/{bookingId}")
    protected BookingDtoToUser update(@RequestHeader("X-Sharer-User-Id") long ownerId,
                                      @RequestParam("approved") boolean approved,
                                      @PathVariable("bookingId") long bookingId) throws AccessDeniedException {
        return bookingService.updateStatus(ownerId, approved, bookingId);
    }

    @GetMapping("/{bookingId}")
    protected BookingDtoToUser findBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                           @PathVariable("bookingId") long bookingId) throws AccessDeniedException {
        return bookingService.findBooking(userId, bookingId);
    }

    @GetMapping
    protected List<BookingDtoToUser> findAllBookingsByBookerId(
            @RequestHeader("X-Sharer-User-Id") long bookerID,
            @RequestParam(name = "state", defaultValue = "ALL") String state) {
        return bookingService.findAllBookingsByBookerId(bookerID, state);
    }

    @GetMapping("/owner")
    protected List<BookingDtoToUser> findAllBookingsByItemOwner(
            @RequestHeader("X-Sharer-User-Id") long ownerId,
            @RequestParam(name = "state", defaultValue = "ALL") String state) {
        return bookingService.findAllBookingsByItemOwner(ownerId, state);
    }
}

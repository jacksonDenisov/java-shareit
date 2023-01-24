package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.ItemDto;

import javax.validation.Valid;
import java.nio.file.AccessDeniedException;

/**
 * TODO Sprint add-bookings.
 */
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
}

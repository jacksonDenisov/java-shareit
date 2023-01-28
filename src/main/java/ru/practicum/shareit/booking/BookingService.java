package ru.practicum.shareit.booking;

import org.springframework.data.domain.Pageable;

import java.nio.file.AccessDeniedException;
import java.util.List;

public interface BookingService {

    BookingDtoToUser create(BookingDtoFromUser bookingDtoFromUser, long bookerId);

    BookingDtoToUser updateStatus(long ownerId, boolean approved, long bookingId) throws AccessDeniedException;

    BookingDtoToUser findBooking(long userId, long bookingId) throws AccessDeniedException;

    List<BookingDtoToUser> findAllBookingsByBookerId(long bookerId, String state, Pageable pageable);

    List<BookingDtoToUser> findAllBookingsByItemOwner(long ownerId, String state, Pageable pageable);
}
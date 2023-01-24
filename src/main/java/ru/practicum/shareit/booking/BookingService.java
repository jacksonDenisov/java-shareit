package ru.practicum.shareit.booking;

import java.nio.file.AccessDeniedException;

public interface BookingService {

    BookingDtoToUser create(BookingDtoFromUser bookingDtoFromUser, long bookerId);

    BookingDtoToUser updateStatus(long ownerId, boolean approved, long bookingId) throws AccessDeniedException;
}

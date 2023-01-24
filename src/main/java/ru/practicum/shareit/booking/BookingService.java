package ru.practicum.shareit.booking;

public interface BookingService {

    BookingDto create(BookingDto bookingDto, long bookerId);
}

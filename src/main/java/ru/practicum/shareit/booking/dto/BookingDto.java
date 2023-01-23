package ru.practicum.shareit.booking.dto;

import lombok.Data;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.in_memory_impl.item.ItemForInMemoryImpl;
import ru.practicum.shareit.in_memory_impl.user.UserForInMemoryImpl;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */

@Data
public class BookingDto {

    private long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private ItemForInMemoryImpl itemForInMemoryImpl;
    private UserForInMemoryImpl booker;
    private BookingStatus status;
}

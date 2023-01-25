package ru.practicum.shareit.booking;

import lombok.Data;
import ru.practicum.shareit.item.ItemDtoForBooking;
import ru.practicum.shareit.user.UserDtoForBooking;

import java.time.LocalDateTime;

@Data
public class BookingDtoToUser {

    private long id;

    private LocalDateTime start;

    private LocalDateTime end;

    private ItemDtoForBooking item;

    private UserDtoForBooking booker;

    private BookingStatus status;
}

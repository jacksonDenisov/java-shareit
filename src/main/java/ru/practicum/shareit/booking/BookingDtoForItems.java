package ru.practicum.shareit.booking;

import lombok.Data;
import ru.practicum.shareit.item.ItemDtoForBooking;

import java.time.LocalDateTime;

@Data
public class BookingDtoForItems {

    private long id;

    private LocalDateTime start;

    private LocalDateTime end;

    private ItemDtoForBooking item;

    private Long bookerId;

    private BookingStatus status;
}

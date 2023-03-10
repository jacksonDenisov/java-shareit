package ru.practicum.shareit.booking;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BookingDtoFromUser {

    private long id;

    private LocalDateTime start;

    private LocalDateTime end;

    private Long itemId;

    private Long bookerId;

    private BookingStatus status;
}

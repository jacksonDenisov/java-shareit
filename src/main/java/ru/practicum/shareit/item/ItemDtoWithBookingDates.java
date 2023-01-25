package ru.practicum.shareit.item;

import lombok.Data;
import ru.practicum.shareit.booking.BookingDtoForItems;

import java.util.List;

@Data
public class ItemDtoWithBookingDates {

    private long id;

    private String name;

    private String description;

    private Boolean available;

    private Long ownerId;

    private Long requestId;

    private List<CommentDto> comments;

    private BookingDtoForItems lastBooking;

    private BookingDtoForItems nextBooking;
}

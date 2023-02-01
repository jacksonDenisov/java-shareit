package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;


@Data
public class ItemDtoBookingDatesAndComments {

    private long id;

    private String name;

    private String description;

    private Boolean available;

    private Long ownerId;

    private Long requestId;

    private List<Comment> comments;

    private Booking lastBooking;

    private Booking nextBooking;


    @Getter
    @Setter
    @AllArgsConstructor
    public static class Booking {

        private long id;

        private Long bookerId;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Comment {

        private long id;

        private String text;

        private String authorName;

        private LocalDateTime created;
    }
}

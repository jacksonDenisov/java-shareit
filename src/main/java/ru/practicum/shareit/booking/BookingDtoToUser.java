package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Data
public class BookingDtoToUser {

    private long id;

    private LocalDateTime start;

    private LocalDateTime end;

    private Item item;

    private User booker;

    private BookingStatus status;


    @Getter
    @Setter
    @AllArgsConstructor
    public static class User {

        private long id;
    }


    @Getter
    @Setter
    @AllArgsConstructor
    public static class Item {

        private long id;

        private String name;
    }
}

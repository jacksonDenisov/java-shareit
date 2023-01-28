package ru.practicum.shareit.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ItemRequestDtoWithReply {

    private long id;

    private String description;

    private LocalDateTime created;

    private List<Item> items;


    @Getter
    @Setter
    @AllArgsConstructor
    public static class Item {

        private long id;

        private String name;

        private String description;

        private Boolean available;

        private Long ownerId;

        private Long requestId;
    }
}
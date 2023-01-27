package ru.practicum.shareit.request;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ItemRequestDtoToUser {

    private long id;

    private String description;

    private LocalDateTime created;
}

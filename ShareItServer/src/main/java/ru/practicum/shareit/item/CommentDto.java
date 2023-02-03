package ru.practicum.shareit.item;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentDto {

    private long id;

    private String text;

    private Long itemId;

    private String authorName;

    private LocalDateTime created;
}

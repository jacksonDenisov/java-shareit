package ru.practicum.shareit.item;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
public class CommentDto {

    private long id;

    @NotBlank
    private String text;

    private Item item;

    private String authorName;

    private LocalDateTime created;
}

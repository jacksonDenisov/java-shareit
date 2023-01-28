package ru.practicum.shareit.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
public class ItemRequestDto {

    private long id;

    @NotBlank
    private String description;

    private LocalDateTime created;
}

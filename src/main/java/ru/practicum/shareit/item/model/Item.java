package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.request.model.ItemRequest;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
public class Item {

    private long id;
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    @NotEmpty
    private boolean available;
    @NotBlank
    private long owner;
}

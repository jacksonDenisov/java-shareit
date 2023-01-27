package ru.practicum.shareit.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ItemRequestDtoFromUser {

    @NotBlank
    private String description;
}

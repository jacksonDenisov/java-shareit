package ru.practicum.shareit.request.dto;

import lombok.Data;
import ru.practicum.shareit.in_memory_impl.user.UserForInMemoryImpl;

import java.util.Date;

/**
 * TODO Sprint add-item-requests.
 */

@Data
public class ItemRequestDto {

    private long id;
    private String description;
    private UserForInMemoryImpl requestor;
    private Date created;
}

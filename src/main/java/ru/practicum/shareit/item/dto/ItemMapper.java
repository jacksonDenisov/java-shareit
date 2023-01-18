package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.item.model.Item;

public class ItemMapper {

    public static Item toItem(ItemDto itemDto, long owner){
        return new Item(0,
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.isAvailable(),
                owner);
    }
}

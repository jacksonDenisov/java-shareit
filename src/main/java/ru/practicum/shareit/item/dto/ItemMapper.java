package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.item.model.Item;

public class ItemMapper {

    public static ItemDto toItemDto(Item item) {
        return new ItemDto(item.getName(),
                item.getDescription(),
                item.isAvailable());
    }

    public static Item toItem(ItemDto itemDto, long itemId, long owner) {
        return new Item(itemId,
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getAvailable(),
                owner);
    }
}
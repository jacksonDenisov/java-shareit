package ru.practicum.shareit.item.service;

import ru.practicum.shareit.in_memory_impl.item.ItemDtoInMemory;
import ru.practicum.shareit.in_memory_impl.item.ItemForInMemoryImpl;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {

    ItemDto create(ItemDto itemDto, long ownerId);

    ItemDto update(ItemDto itemDto, long ownerId, long itemId);

    ItemDto findItem(long itemId);

    List<ItemDto> findAllByOwner(long ownerId);

    //List<ItemDto> searchItems(String text);
}

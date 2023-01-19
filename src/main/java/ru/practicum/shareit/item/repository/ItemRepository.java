package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository {

   Item save(Item item);

   Item update(ItemDto item, long owner, long itemId);

   Item findItem(long itemId);

   List<Item> findAll();
}

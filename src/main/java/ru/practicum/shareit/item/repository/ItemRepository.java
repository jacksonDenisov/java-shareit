package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Map;

public interface ItemRepository {

   Item save(ItemDto itemDto, long owner);

   Item update(ItemDto item, long owner, long itemId);

   Item findItem(long itemId);

   List<Item> findAll();

   Map<Long, Long> getItemsOwners();

   boolean isItemExist(long itemId);
}

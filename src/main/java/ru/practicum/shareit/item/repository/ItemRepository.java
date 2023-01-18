package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;

public interface ItemRepository {

   Item save(Item item);
}

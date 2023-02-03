package ru.practicum.shareit.in_memory_impl.item;

import java.util.List;

public interface ItemServiceInMemory {

    ItemForInMemoryImpl create(ItemDtoInMemory itemDtoInMemory, long owner);

    ItemForInMemoryImpl update(ItemDtoInMemory itemDtoInMemory, long owner, long itemId);

    ItemForInMemoryImpl findItem(long itemId);

    List<ItemForInMemoryImpl> findAllByOwner(long owner);

    List<ItemForInMemoryImpl> searchItems(String text);
}

package ru.practicum.shareit.in_memory_impl.item;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemMapperForInMemoryImpl {

    public static ItemDtoInMemory toItemDto(ItemForInMemoryImpl itemForInMemoryImpl) {
        return new ItemDtoInMemory(itemForInMemoryImpl.getName(),
                itemForInMemoryImpl.getDescription(),
                itemForInMemoryImpl.isAvailable());
    }

    public static ItemForInMemoryImpl toItem(ItemDtoInMemory itemDtoInMemory, long itemId, long owner) {
        return new ItemForInMemoryImpl(itemId,
                itemDtoInMemory.getName(),
                itemDtoInMemory.getDescription(),
                itemDtoInMemory.getAvailable(),
                owner);
    }
}

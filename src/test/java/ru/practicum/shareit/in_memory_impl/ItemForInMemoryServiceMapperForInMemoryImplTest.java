package ru.practicum.shareit.in_memory_impl;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.in_memory_impl.item.ItemDtoInMemory;
import ru.practicum.shareit.in_memory_impl.item.ItemMapperForInMemoryImpl;
import ru.practicum.shareit.in_memory_impl.item.ItemForInMemoryImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ItemForInMemoryServiceMapperForInMemoryImplTest {

    private static long id;
    private static String name;
    private static String description;
    private static boolean available;
    private static long owner;


    @BeforeAll
    static void setUp() {
        id = 1;
        name = "name";
        description = "description";
        available = true;
        owner = 2;
    }


    @Test
    void shouldReturnCorrectItem() {
        ItemDtoInMemory itemDtoInMemory = new ItemDtoInMemory(name, description, available);
        ItemForInMemoryImpl itemForInMemoryImpl = ItemMapperForInMemoryImpl.toItem(itemDtoInMemory, id, owner);
        assertEquals(itemForInMemoryImpl.getId(), id);
        assertEquals(itemForInMemoryImpl.getName(), name);
        assertEquals(itemForInMemoryImpl.getDescription(), description);
        assertEquals(itemForInMemoryImpl.getOwner(), owner);
        assertTrue(itemForInMemoryImpl.isAvailable());
    }

    @Test
    void shouldReturnCorrectItemDto() {
        ItemForInMemoryImpl itemForInMemoryImpl = new ItemForInMemoryImpl(id, name, description, available, owner);
        ItemDtoInMemory itemDtoInMemory = ItemMapperForInMemoryImpl.toItemDto(itemForInMemoryImpl);
        assertEquals(itemDtoInMemory.getName(), name);
        assertEquals(itemDtoInMemory.getDescription(), description);
        assertTrue(itemDtoInMemory.getAvailable());
    }
}

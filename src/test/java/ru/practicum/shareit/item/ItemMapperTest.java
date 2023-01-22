package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ItemMapperTest {

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
        ItemDto itemDto = new ItemDto(name, description, available);
        Item item = ItemMapper.toItem(itemDto, id, owner);
        assertEquals(item.getId(), id);
        assertEquals(item.getName(), name);
        assertEquals(item.getDescription(), description);
        assertEquals(item.getOwner(), owner);
        assertTrue(item.isAvailable());
    }

    @Test
    void shouldReturnCorrectItemDto() {
        Item item = new Item(id, name, description, available, owner);
        ItemDto itemDto = ItemMapper.toItemDto(item);
        assertEquals(itemDto.getName(), name);
        assertEquals(itemDto.getDescription(), description);
        assertTrue(itemDto.getAvailable());
    }
}

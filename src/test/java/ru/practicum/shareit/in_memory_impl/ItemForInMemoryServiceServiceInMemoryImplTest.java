package ru.practicum.shareit.in_memory_impl;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.in_memory_impl.item.ItemDtoInMemory;
import ru.practicum.shareit.in_memory_impl.item.ItemForInMemoryImpl;
import ru.practicum.shareit.in_memory_impl.item.ItemServiceInMemoryImpl;
import ru.practicum.shareit.in_memory_impl.user.UserServiceInMemoryInMemoryImpl;
import ru.practicum.shareit.in_memory_impl.user.UserDtoInMemory;
import ru.practicum.shareit.utils.exeptions.NotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

@SpringBootTest
@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
public class ItemForInMemoryServiceServiceInMemoryImplTest {

    @Autowired
    private ItemServiceInMemoryImpl itemServiceInMemoryImpl;
    @Autowired
    private UserServiceInMemoryInMemoryImpl userServiceInMemoryImpl;

    private static ItemDtoInMemory itemDtoInMemory1;
    private static ItemDtoInMemory itemDtoInMemory2;
    private static ItemDtoInMemory itemDtoInMemory3;
    private static UserDtoInMemory userDtoInMemory1;
    private static UserDtoInMemory userDtoInMemory2;

    @BeforeAll
    static void setUp() {
        userDtoInMemory1 = new UserDtoInMemory("name1", "email1@gmail.com");
        userDtoInMemory2 = new UserDtoInMemory("name2", "email2@gmail.com");
        itemDtoInMemory1 = new ItemDtoInMemory("itemName1", "description1", true);
        itemDtoInMemory2 = new ItemDtoInMemory("itemName2", "description2", false);
        itemDtoInMemory3 = new ItemDtoInMemory("itemName3", "description3", true);
    }


    @Test
    void shouldCreateTwoItemsForOneOwner() {
        userServiceInMemoryImpl.create(userDtoInMemory1);
        assertEquals(itemServiceInMemoryImpl.findAllByOwner(1L).size(), 0);
        itemServiceInMemoryImpl.create(itemDtoInMemory1, 1L);
        itemServiceInMemoryImpl.create(itemDtoInMemory2, 1L);
        assertEquals(itemServiceInMemoryImpl.findAllByOwner(1L).size(), 2);
    }

    @Test
    void shouldThrowNotFoundExceptionWhenTryToCreateItemWithNonExistentOwner() {
        assertThrows(NotFoundException.class, () -> {
            itemServiceInMemoryImpl.create(itemDtoInMemory1, 999L);
        });
    }

    @Test
    void shouldUpdateItem() {
        userServiceInMemoryImpl.create(userDtoInMemory1);
        itemServiceInMemoryImpl.create(itemDtoInMemory1, 1L);
        assertEquals(itemServiceInMemoryImpl.findItem(1L).getName(), "itemName1");
        assertEquals(itemServiceInMemoryImpl.findItem(1L).getDescription(), "description1");
        assertTrue(itemServiceInMemoryImpl.findItem(1L).isAvailable());
        itemServiceInMemoryImpl.update(itemDtoInMemory2, 1L, 1L);
        assertEquals(itemServiceInMemoryImpl.findItem(1L).getName(), "itemName2");
        assertEquals(itemServiceInMemoryImpl.findItem(1L).getDescription(), "description2");
        assertFalse(itemServiceInMemoryImpl.findItem(1L).isAvailable());
    }

    @Test
    void shouldThrowNotFoundExceptionWhenTryToUpdateStrangeItem() {
        userServiceInMemoryImpl.create(userDtoInMemory1);
        userServiceInMemoryImpl.create(userDtoInMemory2);
        itemServiceInMemoryImpl.create(itemDtoInMemory1, 1L);
        assertThrows(NotFoundException.class, () -> {
            itemServiceInMemoryImpl.update(itemDtoInMemory2, 2L, 1L);
        });
    }

    @Test
    void shouldThrowNotFoundExceptionWhenTryToUpdateItemWithNonExistentOwner() {
        userServiceInMemoryImpl.create(userDtoInMemory1);
        itemServiceInMemoryImpl.create(itemDtoInMemory1, 1L);
        assertThrows(NotFoundException.class, () -> {
            itemServiceInMemoryImpl.update(itemDtoInMemory2, 999L, 1L);
        });
    }

    @Test
    void shouldReturnItemById() {
        userServiceInMemoryImpl.create(userDtoInMemory1);
        itemServiceInMemoryImpl.create(itemDtoInMemory1, 1L);
        ItemForInMemoryImpl itemForInMemoryImpl = itemServiceInMemoryImpl.findItem(1L);
        assertEquals(itemForInMemoryImpl.getName(), "itemName1");
    }

    @Test
    void shouldThrowNotFoundExceptionWhenTryToFindItemWithNonExistentOwner() {
        userServiceInMemoryImpl.create(userDtoInMemory1);
        itemServiceInMemoryImpl.create(itemDtoInMemory1, 1L);
        assertThrows(NotFoundException.class, () -> {
            itemServiceInMemoryImpl.findItem(999L);
        });
    }

    @Test
    void shouldReturnItemsByOwnerId() {
        userServiceInMemoryImpl.create(userDtoInMemory1);
        itemServiceInMemoryImpl.create(itemDtoInMemory1, 1L);
        assertEquals(itemServiceInMemoryImpl.findAllByOwner(1L).size(), 1);
        itemServiceInMemoryImpl.create(itemDtoInMemory2, 1L);
        assertEquals(itemServiceInMemoryImpl.findAllByOwner(1L).size(), 2);
    }

    @Test
    void shouldThrowNotFoundExceptionWhenTryToFindAllItemsWithNonExistentOwner() {
        userServiceInMemoryImpl.create(userDtoInMemory1);
        itemServiceInMemoryImpl.create(itemDtoInMemory1, 1L);
        assertThrows(NotFoundException.class, () -> {
            itemServiceInMemoryImpl.findAllByOwner(999L);
        });
    }

    @Test
    void shouldReturnItemsByNameSearchString() {
        userServiceInMemoryImpl.create(userDtoInMemory1);
        itemServiceInMemoryImpl.create(itemDtoInMemory1, 1L);
        assertEquals(itemServiceInMemoryImpl.searchItems("itemName1").size(), 1);
    }

    @Test
    void shouldNotReturnNotAvailableItemsByNameSearchString() {
        userServiceInMemoryImpl.create(userDtoInMemory1);
        itemServiceInMemoryImpl.create(itemDtoInMemory1, 1L);
        itemServiceInMemoryImpl.create(itemDtoInMemory2, 1L);
        itemServiceInMemoryImpl.create(itemDtoInMemory3, 1L);
        assertEquals(itemServiceInMemoryImpl.searchItems("item").size(), 2);
        itemDtoInMemory2.setAvailable(true);
        itemServiceInMemoryImpl.update(itemDtoInMemory2, 1L, 2L);
        assertEquals(itemServiceInMemoryImpl.searchItems("item").size(), 3);
    }

    @Test
    void shouldReturnItemsByDescriptionSearchString() {
        userServiceInMemoryImpl.create(userDtoInMemory1);
        itemServiceInMemoryImpl.create(itemDtoInMemory1, 1L);
        assertEquals(itemServiceInMemoryImpl.searchItems("description").size(), 1);
        itemServiceInMemoryImpl.create(itemDtoInMemory3, 1L);
        assertEquals(itemServiceInMemoryImpl.searchItems("description").size(), 2);

    }

    @Test
    void shouldNotReturnItemsByEmptySearchOrDescriptionSearchString() {
        userServiceInMemoryImpl.create(userDtoInMemory1);
        itemServiceInMemoryImpl.create(itemDtoInMemory1, 1L);
        itemServiceInMemoryImpl.create(itemDtoInMemory2, 1L);
        assertEquals(itemServiceInMemoryImpl.searchItems(" ").size(), 0);
    }
}

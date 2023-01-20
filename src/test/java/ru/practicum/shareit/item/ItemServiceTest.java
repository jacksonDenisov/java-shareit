package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepositoryInMemoryImpl;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.reposiry.UserRepositoryInMemoryImpl;
import ru.practicum.shareit.utils.exeptions.NotFoundException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ItemServiceTest {

    private ItemService itemService;
    private UserService userService;

    private static ItemDto itemDto1;
    private static ItemDto itemDto2;
    private static ItemDto itemDto3;
    private static UserDto userDto1;
    private static UserDto userDto2;

    @BeforeAll
    static void setUp() {
        userDto1 = new UserDto("name1", "email1@gmail.com");
        userDto2 = new UserDto("name2", "email2@gmail.com");
        itemDto1 = new ItemDto("itemName1", "description1", true);
        itemDto2 = new ItemDto("itemName2", "description2", false);
        itemDto3 = new ItemDto("itemName3", "description3", true);
    }

    @BeforeEach
    public void setup() {
        this.userService = new UserService(new UserRepositoryInMemoryImpl());
        this.itemService = new ItemService(
                new ItemRepositoryInMemoryImpl(),
                userService
        );
    }

    @Test
    void shouldCreateTwoItemsForOneOwner() {
        userService.create(userDto1);
        assertEquals(itemService.findAllBNyOwner(1).size(), 0);
        itemService.create(itemDto1, 1);
        itemService.create(itemDto2, 1);
        assertEquals(itemService.findAllBNyOwner(1).size(), 2);
    }

    @Test
    void shouldThrowNotFoundExceptionWhenTryToCreateItemWithNonExistentOwner() {
        assertThrows(NotFoundException.class, () -> {
            itemService.create(itemDto1, 999);
        });
    }

    @Test
    void shouldUpdateItem() {
        userService.create(userDto1);
        itemService.create(itemDto1, 1);
        assertEquals(itemService.findItem(1).getName(), "itemName1");
        assertEquals(itemService.findItem(1).getDescription(), "description1");
        assertTrue(itemService.findItem(1).isAvailable());
        itemService.update(itemDto2, 1, 1);
        assertEquals(itemService.findItem(1).getName(), "itemName2");
        assertEquals(itemService.findItem(1).getDescription(), "description2");
        assertFalse(itemService.findItem(1).isAvailable());
    }

    @Test
    void shouldThrowNotFoundExceptionWhenTryToUpdateStrangeItem() {
        userService.create(userDto1);
        userService.create(userDto2);
        itemService.create(itemDto1, 1);
        assertThrows(NotFoundException.class, () -> {
            itemService.update(itemDto2, 2, 1);
        });
    }

    @Test
    void shouldThrowNotFoundExceptionWhenTryToUpdateItemWithNonExistentOwner() {
        userService.create(userDto1);
        itemService.create(itemDto1, 1);
        assertThrows(NotFoundException.class, () -> {
            itemService.update(itemDto2, 999, 1);
        });
    }

    @Test
    void shouldReturnItemById() {
        userService.create(userDto1);
        itemService.create(itemDto1, 1);
        Item item = itemService.findItem(1);
        assertEquals(item.getName(), "itemName1");
    }

    @Test
    void shouldThrowNotFoundExceptionWhenTryToFindItemWithNonExistentOwner() {
        userService.create(userDto1);
        itemService.create(itemDto1, 1);
        assertThrows(NotFoundException.class, () -> {
            itemService.findItem(999);
        });
    }

    @Test
    void shouldReturnItemsByOwnerId() {
        List<Item> items = new ArrayList<>();
        userService.create(userDto1);
        itemService.create(itemDto1, 1);
        assertEquals(itemService.findAllBNyOwner(1).size(), 1);
        itemService.create(itemDto2, 1);
        assertEquals(itemService.findAllBNyOwner(1).size(), 2);
    }

    @Test
    void shouldThrowNotFoundExceptionWhenTryToFindAllItemsWithNonExistentOwner() {
        userService.create(userDto1);
        itemService.create(itemDto1, 1);
        assertThrows(NotFoundException.class, () -> {
            itemService.findAllBNyOwner(999);
        });
    }

    @Test
    void shouldReturnItemsByNameSearchString() {
        userService.create(userDto1);
        itemService.create(itemDto1, 1);
        assertEquals(itemService.searchItems("itemName1").size(), 1);
    }

    @Test
    void shouldNotReturnNotAvailableItemsByNameSearchString() {
        userService.create(userDto1);
        itemService.create(itemDto1, 1);
        itemService.create(itemDto2, 1);
        itemService.create(itemDto3, 1);
        assertEquals(itemService.searchItems("item").size(), 2);
        itemDto2.setAvailable(true);
        itemService.update(itemDto2, 1, 2);
        assertEquals(itemService.searchItems("item").size(), 3);
    }

    @Test
    void shouldReturnItemsByDescriptionSearchString() {
        userService.create(userDto1);
        itemService.create(itemDto1, 1);
        assertEquals(itemService.searchItems("description").size(), 1);
        itemService.create(itemDto3, 1);
        assertEquals(itemService.searchItems("description").size(), 2);

    }

    @Test
    void shouldNotReturnItemsByEmptySearchOrDescriptionSearchString() {
        userService.create(userDto1);
        itemService.create(itemDto1, 1);
        itemService.create(itemDto2, 1);
        assertEquals(itemService.searchItems(" ").size(), 0);
    }
}

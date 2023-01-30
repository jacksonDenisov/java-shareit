package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ItemRepositoryTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ItemRequestRepository itemRequestRepository;

    private static User user1;
    private static User user2;
    private static Item item1;
    private static Item item2;
    private static ItemRequest itemRequest;

    @BeforeAll
    static void setUp() {
        user1 = new User();
        user1.setName("name1");
        user1.setEmail("user1@email.com");
        user2 = new User();
        user2.setName("name2");
        user2.setEmail("user2@email.com");

        item1 = new Item();
        item1.setName("itemName");
        item1.setDescription("itemDescription");
        item1.setAvailable(true);

        item2 = new Item();
        item2.setName("itemName2");
        item2.setDescription("itemDescription2");
        item2.setAvailable(true);

        itemRequest = new ItemRequest();
        itemRequest.setDescription("RequestDescription");
        itemRequest.setCreated(LocalDateTime.now());
    }

    @Test
    void findAllItemsByRequesterIdTest() {
        User userSaved1 = userRepository.save(user1);
        User userSaved2 = userRepository.save(user2);
        itemRequest.setRequester(userSaved1);
        ItemRequest itemRequestSaved1 = itemRequestRepository.save(itemRequest);
        item1.setRequest(itemRequestSaved1);
        item1.setOwnerId(userSaved2.getId());
        item2.setOwnerId(userSaved1.getId());
        itemRepository.save(item1);
        itemRepository.save(item2);

        assertEquals(itemRepository.findAllItemsByRequesterId(userSaved1.getId()).size(), 1);
        assertEquals(itemRepository.findAllItemsByRequesterId(
                userSaved1.getId()).get(0).getName(), item1.getName());
        assertEquals(itemRepository.findAllItemsByRequesterId(userSaved2.getId()).size(), 0);
    }

    @Test
    void findAllItemsOfOtherUsersTest() {
        User userSaved1 = userRepository.save(user1);
        User userSaved2 = userRepository.save(user2);
        itemRequest.setRequester(userSaved1);
        ItemRequest itemRequestSaved1 = itemRequestRepository.save(itemRequest);
        item1.setRequest(itemRequestSaved1);
        item1.setOwnerId(userSaved2.getId());
        item2.setOwnerId(userSaved1.getId());
        itemRepository.save(item1);
        itemRepository.save(item2);

        assertEquals(itemRepository.findAllItemsOfOtherUsers(userSaved1.getId()).size(), 0);
        assertEquals(itemRepository.findAllItemsOfOtherUsers(userSaved2.getId()).size(), 1);
        assertEquals(itemRepository.findAllItemsOfOtherUsers(
                userSaved2.getId()).get(0).getName(), item1.getName());
    }
}
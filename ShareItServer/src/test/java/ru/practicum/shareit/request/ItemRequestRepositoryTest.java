package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ItemRequestRepositoryTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private ItemRequestRepository itemRequestRepository;

    @Autowired
    private UserRepository userRepository;

    private static User user1;
    private static User user2;

    @BeforeAll
    static void setUp() {
        user1 = new User();
        user1.setName("name1");
        user1.setEmail("user1@email.com");
        user2 = new User();
        user2.setName("name2");
        user2.setEmail("user2@email.com");
    }

    @Test
    public void contextLoads() {
        assertNotNull(em);
    }

    @Test
    void findAllRequestsForOwnerTest() {
        User userSaved1 = userRepository.save(user1);
        User userSaved2 = userRepository.save(user2);
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setDescription("RequestDescription");
        itemRequest.setRequester(userSaved1);
        itemRequest.setCreated(LocalDateTime.now());
        itemRequestRepository.save(itemRequest);

        assertEquals(itemRequestRepository.findAllRequestsForOwner(userSaved1.getId()).size(), 1);
        assertEquals(itemRequestRepository.findAllRequestsForOwner(
                userSaved1.getId()).get(0).getDescription(), itemRequest.getDescription());
        assertEquals(itemRequestRepository.findAllRequestsForOwner(userSaved2.getId()).size(), 0);

    }

    @Test
    void findAllRequestsOfOtherUsersTest() {
        User userSaved1 = userRepository.save(user1);
        User userSaved2 = userRepository.save(user2);
        Pageable pageable = PageRequest.of(0, 10);
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setDescription("RequestDescription");
        itemRequest.setRequester(userSaved1);
        itemRequest.setCreated(LocalDateTime.now());
        itemRequestRepository.save(itemRequest);

        assertEquals(itemRequestRepository.findAllRequestsOfOtherUsers(
                userSaved2.getId(), pageable).toList().size(), 1);
        assertEquals(itemRequestRepository.findAllRequestsOfOtherUsers(
                userSaved2.getId(), pageable).toList().get(0).getDescription(), itemRequest.getDescription());
        assertEquals(itemRequestRepository.findAllRequestsOfOtherUsers(
                userSaved1.getId(), pageable).toList().size(), 0);
    }
}
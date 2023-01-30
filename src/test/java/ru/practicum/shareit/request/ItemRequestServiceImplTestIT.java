package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ItemRequestServiceImplTestIT {

    @Autowired
    private ItemRequestServiceImpl itemRequestService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRequestRepository itemRequestRepository;

    @Test
    void shouldReturnCorrectItemRequestDtoWhenCreateNew() {
        User user = new User();
        user.setName("name");
        user.setEmail("user@user.com");
        User userSaved = userRepository.save(user);
        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setDescription("Описание для запроса вещи");
        ItemRequestDto result = itemRequestService.create(userSaved.getId(), itemRequestDto);
        assertEquals(result.getId(), 1L);
        assertEquals(result.getDescription(), "Описание для запроса вещи");
    }

    @Test
    void shouldThrowNoSuchElementExceptionWhenCreateWithWrongUser() {
        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setDescription("Описание для запроса вещи");
        assertThrows(NoSuchElementException.class, () -> {
            itemRequestService.create(9999L, itemRequestDto);
        });
    }

    @Test
    void shouldFindAllForOneUser() {
        Sort sortByCreated = Sort.by(Sort.Direction.ASC, "created");
        Pageable pageable = PageRequest.of(0, 50, sortByCreated);
        User user = new User();
        user.setName("name");
        user.setEmail("user@user.com");

        User user2 = new User();
        user2.setName("name2");
        user2.setEmail("user2@user.com");
        User userSaved = userRepository.save(user);
        User userSaved2 = userRepository.save(user2);

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(1L);
        itemRequest.setRequester(userSaved2);
        itemRequest.setDescription("Test");
        itemRequest.setCreated(LocalDateTime.now());
        ItemRequest itemRequestSaved = itemRequestRepository.save(itemRequest);

        Item item = new Item();
        item.setName("itemName");
        item.setDescription("itemDesc");
        item.setAvailable(true);
        item.setOwnerId(userSaved.getId());
        item.setRequest(itemRequestSaved);

        List<ItemRequestDtoWithReply> result = itemRequestService.findAllForOneUser(1L, pageable);
        assertEquals(result.size(), 1);
    }
}
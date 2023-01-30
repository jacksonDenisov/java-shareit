package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.utils.exeptions.NotFoundException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class ItemRequestServiceImplTest {

    @Autowired
    private ItemRequestServiceImpl itemRequestService;

    @MockBean
    private ItemRequestRepository itemRequestRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private ItemRepository itemRepository;

    private static ItemRequest itemRequest;
    private static ItemRequestDto itemRequestDto;
    private static User user;
    private static Item item;
    private static List<Item> itemList;
    private static LocalDateTime created;
    private static Pageable pageable;

    @BeforeAll
    static void setUp() {
        pageable = PageRequest.of(0, 50);

        user = new User();
        user.setName("name");
        user.setEmail("user@user.com");
        user.setId(1L);

        created = LocalDateTime.now();

        itemRequest = new ItemRequest();
        itemRequest.setId(1L);
        itemRequest.setRequester(user);
        itemRequest.setDescription("Test");
        itemRequest.setCreated(created);
        itemRequest.setRequester(user);

        item = new Item();
        item.setId(1L);
        item.setName("itemName1");
        item.setDescription("itemDescription1");
        item.setAvailable(true);
        item.setOwnerId(1L);
        item.setRequest(itemRequest);

        itemList = new ArrayList<>();
        itemList.add(item);

        itemRequestDto = new ItemRequestDto();
        itemRequestDto.setDescription("Test");
    }

    @Test
    void shouldThrowNotFoundExceptionWhenCallFindAllByOwnerWithWrongUser() {
        when(userRepository.existsById(any())).thenReturn(false);
        assertThrows(NotFoundException.class, () -> {
            itemRequestService.findAllByOwner(9999L);
        });
    }

    @Test
    void shouldReturnCorrectItemRequestDtoWithReplyWhenCallFindAllByOwner() {
        when(userRepository.existsById(any())).thenReturn(true);
        List<ItemRequest> itemRequests = new ArrayList<>();
        itemRequests.add(itemRequest);
        when(itemRequestRepository.findAllRequestsForOwner(any()))
                .thenReturn(itemRequests);
        when(itemRepository.findAllByRequestId(any())).thenReturn(itemList);

        List<ItemRequestDtoWithReply> result = itemRequestService.findAllByOwner(1L);
        assertEquals(result.size(), 1);
    }

    @Test
    void shouldThrowNotFoundExceptionWhenCallFindAllForOneUserWithWrongUser() {
        when(userRepository.existsById(any())).thenReturn(false);
        assertThrows(NotFoundException.class, () -> {
            itemRequestService.findAllForOneUser(9999L, pageable);
        });
    }

    @Test
    void shouldThrowNotFoundExceptionWhenCallFindFindByIdWithWrongUser() {
        when(userRepository.existsById(any())).thenReturn(false);
        assertThrows(NotFoundException.class, () -> {
            itemRequestService.findById(9999L, 999L);
        });
    }

    @Test
    void shouldReturnCorrectItemRequestDtoWithReplyWhenCallFindById() {
        when(userRepository.existsById(any())).thenReturn(true);
        when(itemRequestRepository.findById(any())).thenReturn(Optional.ofNullable(itemRequest));
        when(itemRepository.findAllByRequestId(any())).thenReturn(itemList);
        ItemRequestDtoWithReply result = itemRequestService.findById(1L, 1L);
        assertEquals(result.getDescription(), "Test");
        assertEquals(result.getItems().get(0).getName(), "itemName1");
    }
}
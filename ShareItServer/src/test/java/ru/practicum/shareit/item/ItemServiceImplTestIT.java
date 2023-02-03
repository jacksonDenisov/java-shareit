package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.utils.exeptions.NotFoundException;
import ru.practicum.shareit.utils.exeptions.ValidationException;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ItemServiceImplTestIT {

    @Autowired
    private ItemServiceImpl itemService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ItemRequestRepository itemRequestRepository;

    private static String name1;
    private static String name2;
    private static String description1;
    private static String description2;
    private static Boolean availableT;
    private static Boolean availableF;
    private static User user1;
    private static User user2;
    private static Pageable pageable;


    @BeforeAll
    static void setup() {
        name1 = "itemName1";
        name2 = "itemName2";
        description1 = "itemDescription1";
        description2 = "itemDescription2";
        availableT = true;
        availableF = false;

        user1 = new User();
        user1.setName("name");
        user1.setEmail("user@user.com");
        user2 = new User();
        user2.setName("name2");
        user2.setEmail("user2@user.com");

        pageable = PageRequest.of(0, 50);
    }

    @Test
    void shouldReturnCorrectItemDtoWhenCreateNewWithoutRequest() {
        userRepository.save(user1);
        ItemDto itemDto = new ItemDto();
        itemDto.setName(name1);
        itemDto.setDescription(description1);
        itemDto.setAvailable(availableT);
        ItemDto result = itemService.create(itemDto, user1.getId());
        assertEquals(result.getName(), name1);
        assertEquals(result.getDescription(), description1);
        assertEquals(result.getAvailable(), availableT);
        assertEquals(result.getOwnerId(), user1.getId());
        assertNull(result.getRequestId());
    }

    @Test
    void shouldReturnCorrectItemDtoWhenCreateNewAndWithRequest() {
        userRepository.save(user1);
        userRepository.save(user2);
        ItemDto itemDto = new ItemDto();
        itemDto.setName(name1);
        itemDto.setDescription(description1);
        itemDto.setAvailable(availableT);
        itemDto.setRequestId(1L);

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(1L);
        itemRequest.setRequester(user1);
        itemRequest.setDescription("Test");
        itemRequest.setCreated(LocalDateTime.now());
        itemRequest.setRequester(user2);
        itemRequestRepository.save(itemRequest);

        ItemDto result = itemService.create(itemDto, user1.getId());
        assertEquals(result.getRequestId(), itemRequest.getId());
    }

    @Test
    void shouldThrowsNotFoundExceptionWhenCreateNewWithWrongUser() {
        ItemDto itemDto = new ItemDto();
        itemDto.setName(name1);
        itemDto.setDescription(description1);
        itemDto.setAvailable(availableT);
        assertThrows(NotFoundException.class, () -> {
            itemService.create(itemDto, 999L);
        });
    }

    @Test
    void shouldUpdateWhenUserIsOk() throws AccessDeniedException {
        userRepository.save(user1);
        userRepository.save(user2);
        ItemDto itemDto = new ItemDto();
        itemDto.setName(name1);
        itemDto.setDescription(description1);
        itemDto.setAvailable(availableT);
        ItemDto result = itemService.create(itemDto, user1.getId());

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setDescription("Test");
        itemRequest.setCreated(LocalDateTime.now());
        itemRequest.setRequester(user2);
        itemRequestRepository.save(itemRequest);

        itemDto.setName(name2);
        itemDto.setDescription(description2);
        itemDto.setAvailable(availableF);
        itemDto.setRequestId(1L);
        ItemDto result2 = itemService.update(itemDto, user1.getId(), result.getId());

        assertEquals(result2.getName(), name2);
        assertEquals(result2.getDescription(), description2);
        assertEquals(result2.getAvailable(), availableF);
        assertEquals(result2.getRequestId(), 1L);

    }

    @Test
    void shouldNotUpdateAndThrowNotFoundExceptionWhenUserIsWrong() {
        userRepository.save(user1);
        ItemDto itemDto = new ItemDto();
        itemDto.setName(name1);
        itemDto.setDescription(description1);
        itemDto.setAvailable(availableT);
        ItemDto result = itemService.create(itemDto, user1.getId());

        userRepository.save(user2);
        itemDto.setName(name2);
        assertThrows(NotFoundException.class, () -> {
            itemService.update(itemDto, user2.getId(), result.getId());
        });
    }

    @Test
    void shouldReturnItemsWhenCallFindAllByOwner() {
        userRepository.save(user1);
        ItemDto itemDto = new ItemDto();
        itemDto.setName(name1);
        itemDto.setDescription(description1);
        itemDto.setAvailable(availableT);
        assertEquals(itemService.findAllByOwner(user1.getId(), pageable).size(), 0);
        itemService.create(itemDto, user1.getId());
        assertEquals(itemService.findAllByOwner(user1.getId(), pageable).size(), 1);
        assertEquals(itemService.findAllByOwner(user1.getId(), pageable).get(0).getName(), name1);
    }

    @Test
    void shouldReturnCorrectListOfItemsWhenCallSearchItems() {
        userRepository.save(user1);
        ItemDto itemDto = new ItemDto();
        itemDto.setName("name1");
        itemDto.setDescription("description1");
        itemDto.setAvailable(availableT);
        itemService.create(itemDto, user1.getId());
        List<ItemDto> itemDtos = itemService.searchItems("name1", pageable);
        assertEquals(itemDtos.size(), 1);
        itemDtos = itemService.searchItems("1", pageable);
        assertEquals(itemDtos.size(), 1);
        itemDtos = itemService.searchItems("deScRipTiOn", pageable);
        assertEquals(itemDtos.size(), 1);
        assertEquals(itemDtos.get(0).getDescription(), "description1");
    }

    @Test
    void shouldReturnCorrectCommentWhenCallAddComment() {
        userRepository.save(user1);
        userRepository.save(user2);
        Item item = new Item();
        item.setName(name1);
        item.setDescription(description1);
        item.setAvailable(availableT);
        item.setOwnerId(user1.getId());
        Item itemSaved = itemRepository.save(item);

        Booking booking = new Booking();
        booking.setStatus(BookingStatus.WAITING);
        booking.setBookerId(user2.getId());
        booking.setItem(itemSaved);
        booking.setStart(LocalDateTime.MIN);
        booking.setEnd(LocalDateTime.MAX);
        bookingRepository.save(booking);

        CommentDto commentDto = new CommentDto();
        commentDto.setText("Test comment");

        CommentDto result = itemService.addComment(item.getId(), user2.getId(), commentDto);
        assertEquals(result.getText(), "Test comment");
        assertEquals(result.getItemId(), item.getId());
        assertEquals(result.getAuthorName(), user2.getName());
    }

    @Test
    void shouldNotReturnCommentWhenCallAddCommentWithWrongUser() {
        userRepository.save(user1);
        userRepository.save(user2);
        Item item = new Item();
        item.setName(name1);
        item.setDescription(description1);
        item.setAvailable(availableT);
        item.setOwnerId(user1.getId());
        Item itemSaved = itemRepository.save(item);

        Booking booking = new Booking();
        booking.setStatus(BookingStatus.WAITING);
        booking.setBookerId(user2.getId());
        booking.setItem(itemSaved);
        booking.setStart(LocalDateTime.MIN);
        booking.setEnd(LocalDateTime.MAX);
        bookingRepository.save(booking);

        CommentDto commentDto = new CommentDto();
        commentDto.setText("Test comment");

        assertThrows(ValidationException.class, () -> {
            itemService.addComment(item.getId(), user1.getId(), commentDto);
        });
    }

    @Test
    void findItemTest() {
        userRepository.save(user1);
        ItemDto itemDto = new ItemDto();
        itemDto.setName(name1);
        itemDto.setDescription(description1);
        itemDto.setAvailable(availableT);
        itemService.create(itemDto, user1.getId());
        ItemDtoBookingDatesAndComments result = itemService.findItem(1L, 1L);
        assertEquals(result.getName(), itemDto.getName());
        assertEquals(result.getDescription(), itemDto.getDescription());
    }
}
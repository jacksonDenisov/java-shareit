package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.utils.exeptions.NotFoundException;
import ru.practicum.shareit.utils.exeptions.ValidationException;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

    private static String name1;
    private static String name2;
    private static String description1;
    private static String description2;
    private static Boolean availableT;
    private static Boolean availableF;
    private static User user1;
    private static User user2;


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
    }

    @Test
    void shouldReturnCorrectItemDtoWhenCreateNew() {
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
    }

    @Test
    void shouldUpdateWhenUserIsOk() throws AccessDeniedException {
        userRepository.save(user1);
        ItemDto itemDto = new ItemDto();
        itemDto.setName(name1);
        itemDto.setDescription(description1);
        itemDto.setAvailable(availableT);
        ItemDto result = itemService.create(itemDto, user1.getId());

        itemDto.setName(name2);
        itemDto.setDescription(description2);
        itemDto.setAvailable(availableF);
        ItemDto result2 = itemService.update(itemDto, user1.getId(), result.getId());

        assertEquals(result2.getName(), name2);
        assertEquals(result2.getDescription(), description2);
        assertEquals(result2.getAvailable(), availableF);
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
        assertEquals(itemService.findAllByOwner(user1.getId()).size(), 0);
        itemService.create(itemDto, user1.getId());
        assertEquals(itemService.findAllByOwner(user1.getId()).size(), 1);
        assertEquals(itemService.findAllByOwner(user1.getId()).get(0).getName(), name1);
    }

    @Test
    void shouldReturnCorrectListOfItemsWhenCallSearchItems() {
        userRepository.save(user1);
        ItemDto itemDto = new ItemDto();
        itemDto.setName("name1");
        itemDto.setDescription("description1");
        itemDto.setAvailable(availableT);
        itemService.create(itemDto, user1.getId());
        List<ItemDto> itemDtos = itemService.searchItems("name1");
        assertEquals(itemDtos.size(), 1);
        itemDtos = itemService.searchItems("1");
        assertEquals(itemDtos.size(), 1);
        itemDtos = itemService.searchItems("deScRipTiOn");
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
}
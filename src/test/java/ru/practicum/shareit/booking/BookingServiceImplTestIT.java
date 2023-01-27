package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.utils.exeptions.NotFoundException;
import ru.practicum.shareit.utils.exeptions.ValidationException;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class BookingServiceImplTestIT {

    @Autowired
    private BookingServiceImpl bookingService;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    private static User user1;
    private static User user2;
    private static Item item1;

    @BeforeAll
    static void setup() {
        item1 = new Item();
        item1.setName("itemName1");
        item1.setDescription("itemDescription1");
        item1.setAvailable(true);
        item1.setOwnerId(1L);

        user1 = new User();
        user1.setName("name");
        user1.setEmail("user@user.com");
        user2 = new User();
        user2.setName("name2");
        user2.setEmail("user2@user.com");
    }

    @Test
    void shouldReturnCorrectBookingWhenCreateNew() {
        userRepository.save(user1);
        User booker = userRepository.save(user2);
        Item item = itemRepository.save(item1);
        BookingDtoFromUser bookingDtoFromUser = new BookingDtoFromUser();
        bookingDtoFromUser.setItemId(item.getId());
        bookingDtoFromUser.setStart(LocalDateTime.MIN);
        bookingDtoFromUser.setEnd(LocalDateTime.MAX);
        BookingDtoToUser result = bookingService.create(bookingDtoFromUser, booker.getId());

        assertEquals(result.getId(), 1L);
        assertEquals(result.getStart(), LocalDateTime.MIN);
        assertEquals(result.getEnd(), LocalDateTime.MAX);
        assertEquals(result.getStatus(), BookingStatus.WAITING);
        assertEquals(result.getItem().getName(), item.getName());
        assertEquals(result.getBooker().getId(), booker.getId());
    }

    @Test
    void shouldNotReturnBookingWhenCreateNewWithWrongUser() {
        User owner = userRepository.save(user1);
        User booker = userRepository.save(user2);
        Item item = itemRepository.save(item1);
        BookingDtoFromUser bookingDtoFromUser = new BookingDtoFromUser();
        bookingDtoFromUser.setItemId(item.getId());
        bookingDtoFromUser.setStart(LocalDateTime.MIN);
        bookingDtoFromUser.setEnd(LocalDateTime.MAX);

        assertThrows(NotFoundException.class, () -> {
            bookingService.create(bookingDtoFromUser, 999999L);
        });
        assertThrows(NotFoundException.class, () -> {
            bookingService.create(bookingDtoFromUser, owner.getId());
        });

        bookingDtoFromUser.setStart(LocalDateTime.MAX);
        bookingDtoFromUser.setEnd(LocalDateTime.MIN);
        assertThrows(ValidationException.class, () -> {
            bookingService.create(bookingDtoFromUser, booker.getId());
        });
    }

    @Test
    void shouldChangeStatusToApproveAndRejectWhenCallUpdate() throws AccessDeniedException {
        User owner = userRepository.save(user1);
        User booker = userRepository.save(user2);
        Item item = itemRepository.save(item1);
        BookingDtoFromUser bookingDtoFromUser = new BookingDtoFromUser();
        bookingDtoFromUser.setItemId(item.getId());
        bookingDtoFromUser.setStart(LocalDateTime.MIN);
        bookingDtoFromUser.setEnd(LocalDateTime.MAX);
        BookingDtoToUser booking = bookingService.create(bookingDtoFromUser, booker.getId());
        assertEquals(booking.getStatus(), BookingStatus.WAITING);

        BookingDtoToUser bookingApproved = bookingService.updateStatus(owner.getId(), true, booking.getId());
        assertEquals(bookingApproved.getStatus(), BookingStatus.APPROVED);

        BookingDtoToUser bookingRejected = bookingService.updateStatus(owner.getId(), false, booking.getId());
        assertEquals(bookingRejected.getStatus(), BookingStatus.REJECTED);
    }

    @Test
    void shouldNotChangeStatusWhenCallUpdateWithWrongData() throws AccessDeniedException {
        User owner = userRepository.save(user1);
        User booker = userRepository.save(user2);
        Item item = itemRepository.save(item1);
        BookingDtoFromUser bookingDtoFromUser = new BookingDtoFromUser();
        bookingDtoFromUser.setItemId(item.getId());
        bookingDtoFromUser.setStart(LocalDateTime.MIN);
        bookingDtoFromUser.setEnd(LocalDateTime.MAX);
        BookingDtoToUser booking = bookingService.create(bookingDtoFromUser, booker.getId());
        assertEquals(booking.getStatus(), BookingStatus.WAITING);

        assertThrows(AccessDeniedException.class, () -> {
            bookingService.updateStatus(9999L, true, booking.getId());
        });

        BookingDtoToUser bookingApproved = bookingService.updateStatus(owner.getId(), true, booking.getId());
        assertEquals(bookingApproved.getStatus(), BookingStatus.APPROVED);
        assertThrows(ValidationException.class, () -> {
            bookingService.updateStatus(owner.getId(), true, booking.getId());
        });
    }

    @Test
    void shouldReturnListOfAllBookingsByBookerId() {
        userRepository.save(user1);
        User booker = userRepository.save(user2);
        Item item = itemRepository.save(item1);
        BookingDtoFromUser bookingDtoFromUser = new BookingDtoFromUser();
        bookingDtoFromUser.setItemId(item.getId());
        bookingDtoFromUser.setStart(LocalDateTime.MIN);
        bookingDtoFromUser.setEnd(LocalDateTime.MAX);
        assertEquals(bookingService.findAllBookingsByBookerId(booker.getId(), "ALL").size(), 0);
        bookingService.create(bookingDtoFromUser, booker.getId());
        assertEquals(bookingService.findAllBookingsByBookerId(booker.getId(), "ALL").size(), 1);

        assertThrows(NotFoundException.class, () -> {
            bookingService.findAllBookingsByBookerId(9999L, "ALL");
        });
    }

    @Test
    void shouldReturnListOfAllBookingsByOwnerId() {
        User owner = userRepository.save(user1);
        User booker = userRepository.save(user2);
        Item item = itemRepository.save(item1);
        BookingDtoFromUser bookingDtoFromUser = new BookingDtoFromUser();
        bookingDtoFromUser.setItemId(item.getId());
        bookingDtoFromUser.setStart(LocalDateTime.MIN);
        bookingDtoFromUser.setEnd(LocalDateTime.MAX);
        assertEquals(bookingService.findAllBookingsByItemOwner(owner.getId(), "ALL").size(), 0);
        bookingService.create(bookingDtoFromUser, booker.getId());
        assertEquals(bookingService.findAllBookingsByItemOwner(owner.getId(), "ALL").size(), 1);

        assertThrows(NotFoundException.class, () -> {
            bookingService.findAllBookingsByItemOwner(9999L, "ALL");
        });
    }
}
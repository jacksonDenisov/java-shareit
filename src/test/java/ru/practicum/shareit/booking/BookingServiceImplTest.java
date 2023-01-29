package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestDto;
import ru.practicum.shareit.user.User;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.when;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class BookingServiceImplTest {

    @Autowired
    private BookingServiceImpl bookingService;

    @MockBean
    private BookingRepository bookingRepository;

    private static ItemRequest itemRequest;
    private static ItemRequestDto itemRequestDto;
    private static User user;
    private static Item item;
    private static List<Item> itemList;
    private static LocalDateTime created;

    private static Booking booking;

    @BeforeAll
    static void setUp() {

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

        booking = new Booking();
        booking.setId(1L);
        booking.setBookerId(1L);
        booking.setItem(item);
        booking.setStart(LocalDateTime.MAX);
        booking.setEnd(LocalDateTime.MAX);
        booking.setStatus(BookingStatus.WAITING);
    }

    @Test
    void shouldFindBooking() throws Exception {
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.ofNullable(booking));
        BookingDtoToUser bookingDtoToUser = bookingService.findBooking(1L, 1L);
        assertEquals(bookingDtoToUser.getId(), 1L);
        assertEquals(bookingDtoToUser.getBooker().getId(), booking.getBookerId());
        assertEquals(bookingDtoToUser.getItem().getName(), item.getName());
    }

    @Test
    void shouldThrowsAccessDeniedExceptionFindBookingWhenUserWrong() {
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.ofNullable(booking));
        assertThrows(AccessDeniedException.class, () -> {
            bookingService.findBooking(999L, 1L);
        });
    }
}
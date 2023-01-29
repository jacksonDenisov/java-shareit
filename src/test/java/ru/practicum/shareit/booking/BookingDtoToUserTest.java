package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class BookingDtoToUserJsonTest {

    @Autowired
    private JacksonTester<BookingDtoToUser> json;

    @Test
    void testBookingDtoToUser() throws Exception {
        BookingDtoToUser bookingDtoToUser = new BookingDtoToUser();
        bookingDtoToUser.setId(1L);
        bookingDtoToUser.setStart(LocalDateTime.MAX);
        bookingDtoToUser.setEnd(LocalDateTime.MAX);
        bookingDtoToUser.setStatus(BookingStatus.WAITING);
        bookingDtoToUser.setBooker(new BookingDtoToUser.User(2L));
        bookingDtoToUser.setItem(new BookingDtoToUser.Item(3L, "itemName"));

        JsonContent<BookingDtoToUser> result = json.write(bookingDtoToUser);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathValue("$.start").isEqualTo(bookingDtoToUser.getStart().toString());
        assertThat(result).extractingJsonPathValue("$.end").isEqualTo(bookingDtoToUser.getEnd().toString());
        assertThat(result).extractingJsonPathValue("$.status").isEqualTo("WAITING");
        assertThat(result).extractingJsonPathNumberValue("$.booker.id").isEqualTo(2);
        assertThat(result).extractingJsonPathNumberValue("$.item.id").isEqualTo(3);
        assertThat(result).extractingJsonPathStringValue("$.item.name").isEqualTo("itemName");
    }
}
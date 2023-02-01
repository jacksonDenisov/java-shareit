package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class BookingDtoFromUserTest {

    @Autowired
    private JacksonTester<BookingDtoFromUser> json;

    @Test
    void testBookingDtoFromUser() throws Exception {
        BookingDtoFromUser bookingDtoFromUser = new BookingDtoFromUser();
        bookingDtoFromUser.setId(1L);
        bookingDtoFromUser.setStart(LocalDateTime.MAX);
        bookingDtoFromUser.setEnd(LocalDateTime.MAX);
        bookingDtoFromUser.setBookerId(2L);
        bookingDtoFromUser.setItemId(3L);
        bookingDtoFromUser.setStatus(BookingStatus.WAITING);

        JsonContent<BookingDtoFromUser> result = json.write(bookingDtoFromUser);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathValue("$.start").isEqualTo(bookingDtoFromUser.getStart().toString());
        assertThat(result).extractingJsonPathValue("$.end").isEqualTo(bookingDtoFromUser.getEnd().toString());
        assertThat(result).extractingJsonPathValue("$.status").isEqualTo("WAITING");
        assertThat(result).extractingJsonPathNumberValue("$.bookerId").isEqualTo(2);
        assertThat(result).extractingJsonPathNumberValue("$.itemId").isEqualTo(3);
    }
}
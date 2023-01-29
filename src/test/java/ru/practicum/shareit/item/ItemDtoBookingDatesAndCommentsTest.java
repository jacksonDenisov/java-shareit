package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ItemDtoBookingDatesAndCommentsTest {
    @Autowired
    private JacksonTester<ItemDtoBookingDatesAndComments> json;

    @Test
    void testItemDtoBookingDatesAndComments() throws Exception {
        ItemDtoBookingDatesAndComments.Comment comment =
                new ItemDtoBookingDatesAndComments.Comment(1L, "text", "authorName", LocalDateTime.MIN);
        List<ItemDtoBookingDatesAndComments.Comment> commentList = new ArrayList<>();
        commentList.add(comment);

        ItemDtoBookingDatesAndComments.Booking lastBooking = new ItemDtoBookingDatesAndComments.Booking(1L, 2L);
        ItemDtoBookingDatesAndComments.Booking nextBooking = new ItemDtoBookingDatesAndComments.Booking(3L, 4L);

        ItemDtoBookingDatesAndComments itemDtoBookingDatesAndComments = new ItemDtoBookingDatesAndComments();
        itemDtoBookingDatesAndComments.setId(1L);
        itemDtoBookingDatesAndComments.setName("name");
        itemDtoBookingDatesAndComments.setDescription("description");
        itemDtoBookingDatesAndComments.setAvailable(true);
        itemDtoBookingDatesAndComments.setOwnerId(2L);
        itemDtoBookingDatesAndComments.setRequestId(3L);
        itemDtoBookingDatesAndComments.setComments(commentList);
        itemDtoBookingDatesAndComments.setLastBooking(lastBooking);
        itemDtoBookingDatesAndComments.setNextBooking(nextBooking);

        JsonContent<ItemDtoBookingDatesAndComments> result = json.write(itemDtoBookingDatesAndComments);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("name");
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("description");
        assertThat(result).extractingJsonPathBooleanValue("$.available").isEqualTo(true);
        assertThat(result).extractingJsonPathNumberValue("$.ownerId").isEqualTo(2);
        assertThat(result).extractingJsonPathNumberValue("$.requestId").isEqualTo(3);
        assertThat(result).extractingJsonPathNumberValue("$.comments.[0].id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.comments.[0].text").isEqualTo("text");
        assertThat(result).extractingJsonPathStringValue("$.comments.[0].authorName").isEqualTo("authorName");
        assertThat(result).extractingJsonPathNumberValue("$.lastBooking.id").isEqualTo(1);
        assertThat(result).extractingJsonPathNumberValue("$.lastBooking.bookerId").isEqualTo(2);
        assertThat(result).extractingJsonPathNumberValue("$.nextBooking.id").isEqualTo(3);
        assertThat(result).extractingJsonPathNumberValue("$.nextBooking.bookerId").isEqualTo(4);
    }
}
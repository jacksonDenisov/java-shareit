package ru.practicum.shareit.request;

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
class ItemRequestDtoWithReplyTest {

    @Autowired
    private JacksonTester<ItemRequestDtoWithReply> json;

    @Test
    void testItemRequestDtoWithReply() throws Exception {
        ItemRequestDtoWithReply.Item item = new ItemRequestDtoWithReply.Item(1L,
                "itemName", "itemDescription", true, 2L, 3L);
        List<ItemRequestDtoWithReply.Item> items = new ArrayList<>();
        items.add(item);

        ItemRequestDtoWithReply itemRequestDtoWithReply = new ItemRequestDtoWithReply();
        itemRequestDtoWithReply.setId(4L);
        itemRequestDtoWithReply.setDescription("description");
        itemRequestDtoWithReply.setCreated(LocalDateTime.MIN);
        itemRequestDtoWithReply.setItems(items);

        JsonContent<ItemRequestDtoWithReply> result = json.write(itemRequestDtoWithReply);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(4);
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("description");
        assertThat(result).extractingJsonPathStringValue("$.created")
                .isEqualTo("-999999999-01-01T00:00:00");
        assertThat(result).extractingJsonPathNumberValue("$.items.[0].id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.items.[0].name").isEqualTo("itemName");
        assertThat(result).extractingJsonPathStringValue("$.items.[0].description")
                .isEqualTo("itemDescription");
        assertThat(result).extractingJsonPathBooleanValue("$.items.[0].available").isEqualTo(true);
        assertThat(result).extractingJsonPathNumberValue("$.items.[0].ownerId").isEqualTo(2);
        assertThat(result).extractingJsonPathNumberValue("$.items.[0].requestId").isEqualTo(3);
    }
}
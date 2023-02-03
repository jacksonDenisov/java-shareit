package ru.practicum.shareit.request;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemRequestMapper {

    public static ItemRequest toItemRequest(User requester,
                                            ItemRequestDto itemRequestDto,
                                            LocalDateTime created) {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setRequester(requester);
        itemRequest.setDescription(itemRequestDto.getDescription());
        itemRequest.setCreated(created);
        return itemRequest;
    }

    public static ItemRequestDto toItemRequestDto(ItemRequest itemRequest) {
        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setId(itemRequest.getId());
        itemRequestDto.setDescription(itemRequest.getDescription());
        itemRequestDto.setCreated(itemRequest.getCreated());
        return itemRequestDto;
    }

    public static ItemRequestDtoWithReply toItemRequestDtoWithReply(ItemRequest itemRequest, List<Item> items) {
        ItemRequestDtoWithReply itemRequestDtoWithReply = new ItemRequestDtoWithReply();
        itemRequestDtoWithReply.setId(itemRequest.getId());
        itemRequestDtoWithReply.setDescription(itemRequest.getDescription());
        itemRequestDtoWithReply.setCreated(itemRequest.getCreated());
        itemRequestDtoWithReply.setItems(toItemRequestDtoWithReply_Item(items));
        return itemRequestDtoWithReply;
    }

    public static ItemRequestDtoWithReply.Item toItemRequestDtoWithReply_Item(Item item) {
        return new ItemRequestDtoWithReply.Item(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getOwnerId(),
                item.getRequest().getId());
    }

    public static List<ItemRequestDtoWithReply.Item> toItemRequestDtoWithReply_Item(List<Item> items) {
        List<ItemRequestDtoWithReply.Item> convertedItems = new ArrayList<>();
        for (Item item : items) {
            convertedItems.add(toItemRequestDtoWithReply_Item(item));
        }
        return convertedItems;
    }
}
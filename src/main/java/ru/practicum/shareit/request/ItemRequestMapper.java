package ru.practicum.shareit.request;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemRequestMapper {

    public static ItemRequest toItemRequest(User requester,
                                            ItemRequestDtoFromUser itemRequestDtoFromUser,
                                            LocalDateTime created) {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setRequester(requester);
        itemRequest.setDescription(itemRequestDtoFromUser.getDescription());
        itemRequest.setCreated(created);
        return itemRequest;
    }

    public static ItemRequestDtoToUser toItemRequestDtoToUser(ItemRequest itemRequest){
        ItemRequestDtoToUser itemRequestDtoToUser = new ItemRequestDtoToUser();
        itemRequestDtoToUser.setId(itemRequest.getId());
        itemRequestDtoToUser.setDescription(itemRequest.getDescription());
        itemRequestDtoToUser.setCreated(itemRequest.getCreated());
        return itemRequestDtoToUser;
    }
}

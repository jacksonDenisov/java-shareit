package ru.practicum.shareit.request;

import java.util.List;

public interface ItemRequestService {

    ItemRequestDtoToUser create(long requesterId, ItemRequestDtoFromUser itemRequestDtoFromUser);

    List<ItemRequestDtoToUser> findAllByOwner(long ownerId);

    List<ItemRequestDtoToUser> findAll();

    ItemRequestDtoToUser findById(long requestId);
}

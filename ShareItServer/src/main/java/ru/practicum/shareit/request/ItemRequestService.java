package ru.practicum.shareit.request;

import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ItemRequestService {

    ItemRequestDto create(long requesterId, ItemRequestDto itemRequestDto);

    List<ItemRequestDtoWithReply> findAllByOwner(long ownerId);

    List<ItemRequestDtoWithReply> findAllForOneUser(long userId, Pageable pageable);

    ItemRequestDtoWithReply findById(long requestId, long userId);
}
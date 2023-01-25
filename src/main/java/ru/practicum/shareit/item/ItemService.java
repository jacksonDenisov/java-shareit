package ru.practicum.shareit.item;

import java.nio.file.AccessDeniedException;
import java.util.List;

public interface ItemService {

    ItemDto create(ItemDto itemDto, long ownerId);

    ItemDto update(ItemDto itemDto, long ownerId, long itemId) throws AccessDeniedException;

    ItemDtoWithBookingDates findItem(long itemId, long userId);

    List<ItemDtoWithBookingDates> findAllByOwner(long ownerId);

    List<ItemDto> searchItems(String text);

    CommentDto addComment(long itemId, long userId, CommentDto commentDto);
}

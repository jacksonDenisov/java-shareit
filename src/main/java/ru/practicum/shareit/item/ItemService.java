package ru.practicum.shareit.item;

import org.springframework.data.domain.Pageable;

import java.nio.file.AccessDeniedException;
import java.util.List;

public interface ItemService {

    ItemDto create(ItemDto itemDto, long ownerId);

    ItemDto update(ItemDto itemDto, long ownerId, long itemId) throws AccessDeniedException;

    ItemDtoBookingDatesAndComments findItem(long itemId, long userId);

    List<ItemDtoBookingDatesAndComments> findAllByOwner(long ownerId, Pageable pageable);

    List<ItemDto> searchItems(String text, Pageable pageable);

    CommentDto addComment(long itemId, long userId, CommentDto commentDto);
}
package ru.practicum.shareit.item;

import java.nio.file.AccessDeniedException;
import java.util.List;

public interface ItemService {

    ItemDto create(ItemDto itemDto, long ownerId);

    ItemDto update(ItemDto itemDto, long ownerId, long itemId) throws AccessDeniedException;

    ItemDto findItem(long itemId);

    List<ItemDto> findAllByOwner(long ownerId);

    List<ItemDto> searchItems(String text);

    Boolean isItemAvailable(long itemId);

    long findOwnerIdByItemId(long itemId);

    ItemDtoForBooking findItemForBooking(long itemId);
}

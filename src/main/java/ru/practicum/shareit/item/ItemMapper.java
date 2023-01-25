package ru.practicum.shareit.item;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingMapper;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemMapper {

    public static Item toItemNew(ItemDto itemDto, long ownerId) {
        Item item = new Item();
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setAvailable(itemDto.getAvailable());
        item.setOwnerId(ownerId);
        if (itemDto.getRequestId() != null) {
            item.setRequestId(itemDto.getRequestId());
        }
        return item;
    }

    public static ItemDtoForBooking toItemDtoForBooking(Item item) {
        ItemDtoForBooking itemDtoForBooking = new ItemDtoForBooking();
        itemDtoForBooking.setId(item.getId());
        itemDtoForBooking.setName(item.getName());
        return itemDtoForBooking;
    }

    public static ItemDtoWithBookingDates toItemDtoWithBookingDates(Item item,
                                                                    Booking lastBooking,
                                                                    Booking nextBooking,
                                                                    List<CommentDto> comments) {
        ItemDtoWithBookingDates itemDtoWithBookingDates = new ItemDtoWithBookingDates();
        itemDtoWithBookingDates.setId(item.getId());
        itemDtoWithBookingDates.setName(item.getName());
        itemDtoWithBookingDates.setDescription(item.getDescription());
        itemDtoWithBookingDates.setAvailable(item.getAvailable());
        itemDtoWithBookingDates.setOwnerId(item.getOwnerId());
        itemDtoWithBookingDates.setComments(comments);
        if (item.getRequestId() != null) {
            itemDtoWithBookingDates.setRequestId(item.getRequestId());
        }
        if (lastBooking != null) {
            itemDtoWithBookingDates.setLastBooking(BookingMapper.toBookingDtoForItems(lastBooking));
        }
        if (nextBooking != null) {
            itemDtoWithBookingDates.setNextBooking(BookingMapper.toBookingDtoForItems(nextBooking));
        }
        return itemDtoWithBookingDates;
    }

    public static ItemDtoWithBookingDates toItemDtoWithoutBookingDates(Item item, List<CommentDto> comments) {
        ItemDtoWithBookingDates itemDtoWithBookingDates = new ItemDtoWithBookingDates();
        itemDtoWithBookingDates.setId(item.getId());
        itemDtoWithBookingDates.setName(item.getName());
        itemDtoWithBookingDates.setDescription(item.getDescription());
        itemDtoWithBookingDates.setAvailable(item.getAvailable());
        itemDtoWithBookingDates.setOwnerId(item.getOwnerId());
        itemDtoWithBookingDates.setComments(comments);
        if (item.getRequestId() != null) {
            itemDtoWithBookingDates.setRequestId(item.getRequestId());
        }
        return itemDtoWithBookingDates;
    }

    public static ItemDto toItemDto(Item item) {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        itemDto.setAvailable(item.getAvailable());
        itemDto.setOwnerId(item.getOwnerId());
        if (item.getRequestId() != null) {
            itemDto.setRequestId(item.getRequestId());
        }
        return itemDto;
    }

    public static List<ItemDto> toItemDto(List<Item> items) {
        List<ItemDto> dtos = new ArrayList<>();
        for (Item item : items) {
            dtos.add(toItemDto(item));
        }
        return dtos;
    }
}

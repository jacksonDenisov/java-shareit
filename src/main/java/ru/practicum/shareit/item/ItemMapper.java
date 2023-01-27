package ru.practicum.shareit.item;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.Booking;

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

    public static ItemDtoBookingDatesAndComments toItemDtoWithBookingDates(Item item,
                                                                           Booking lastBooking,
                                                                           Booking nextBooking,
                                                                           List<Comment> comments) {
        ItemDtoBookingDatesAndComments itemDtoBookingDatesAndComments = new ItemDtoBookingDatesAndComments();
        itemDtoBookingDatesAndComments.setId(item.getId());
        itemDtoBookingDatesAndComments.setName(item.getName());
        itemDtoBookingDatesAndComments.setDescription(item.getDescription());
        itemDtoBookingDatesAndComments.setAvailable(item.getAvailable());
        itemDtoBookingDatesAndComments.setOwnerId(item.getOwnerId());
        itemDtoBookingDatesAndComments.setComments(toItemDto_Comments(comments));
        if (item.getRequestId() != null) {
            itemDtoBookingDatesAndComments.setRequestId(item.getRequestId());
        }
        if (lastBooking != null) {
            itemDtoBookingDatesAndComments.setLastBooking(new ItemDtoBookingDatesAndComments.Booking(
                    lastBooking.getId(),
                    lastBooking.getBookerId()));
        }
        if (nextBooking != null) {
            itemDtoBookingDatesAndComments.setNextBooking(new ItemDtoBookingDatesAndComments.Booking(
                    nextBooking.getId(),
                    nextBooking.getBookerId()));
        }
        return itemDtoBookingDatesAndComments;
    }

    public static ItemDtoBookingDatesAndComments toItemDtoWithoutBookingDates(Item item, List<Comment> comments) {
        ItemDtoBookingDatesAndComments itemDtoBookingDatesAndComments = new ItemDtoBookingDatesAndComments();
        itemDtoBookingDatesAndComments.setId(item.getId());
        itemDtoBookingDatesAndComments.setName(item.getName());
        itemDtoBookingDatesAndComments.setDescription(item.getDescription());
        itemDtoBookingDatesAndComments.setAvailable(item.getAvailable());
        itemDtoBookingDatesAndComments.setOwnerId(item.getOwnerId());
        itemDtoBookingDatesAndComments.setComments(toItemDto_Comments(comments));
        if (item.getRequestId() != null) {
            itemDtoBookingDatesAndComments.setRequestId(item.getRequestId());
        }
        return itemDtoBookingDatesAndComments;
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

    public static ItemDtoBookingDatesAndComments.Comment toItemDto_Comment(Comment comment) {
        return new ItemDtoBookingDatesAndComments.Comment(
                comment.getId(),
                comment.getText(),
                comment.getAuthor().getName(),
                comment.getCreated()
        );
    }

    public static List<ItemDto> toItemDto(List<Item> items) {
        List<ItemDto> dtos = new ArrayList<>();
        for (Item item : items) {
            dtos.add(toItemDto(item));
        }
        return dtos;
    }

    public static List<ItemDtoBookingDatesAndComments.Comment> toItemDto_Comments(List<Comment> comments) {
        List<ItemDtoBookingDatesAndComments.Comment> convertedComments = new ArrayList<>();
        for (Comment comment : comments) {
            convertedComments.add(toItemDto_Comment(comment));
        }
        return convertedComments;
    }
}

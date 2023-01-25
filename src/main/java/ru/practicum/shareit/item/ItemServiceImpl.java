package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.utils.exeptions.NotFoundException;
import ru.practicum.shareit.utils.exeptions.ValidationException;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;

    private final BookingRepository bookingRepository;

    private final CommentRepository commentRepository;

    private final UserRepository userRepository;


    @Override
    @Transactional
    public ItemDto create(ItemDto itemDto, long ownerId) {
        if (userRepository.existsById(ownerId)) {
            Item item = itemRepository.save(ItemMapper.toItemNew(itemDto, ownerId));
            return ItemMapper.toItemDto(item);
        } else {
            throw new NotFoundException("Пользователь не найден!");
        }
    }

    @Override
    @Transactional
    public ItemDto update(ItemDto itemDto, long ownerId, long itemId) throws AccessDeniedException {
        Item item = itemRepository.findById(itemId).get();
        if (item.getOwnerId() != null && item.getOwnerId().equals(ownerId)) {
            if (itemDto.getName() != null) {
                item.setName(itemDto.getName());
            }
            if (itemDto.getDescription() != null) {
                item.setDescription(itemDto.getDescription());
            }
            if (itemDto.getAvailable() != null) {
                item.setAvailable(itemDto.getAvailable());
            }
            if (itemDto.getRequestId() != null) {
                item.setRequestId(itemDto.getRequestId());
            }
            Item itemChanged = itemRepository.save(item);
            return ItemMapper.toItemDto(itemChanged);
        } else {
            throw new NotFoundException("Такая вещь у пользователя не найдена!");
        }
    }

    @Override
    public ItemDtoWithBookingDates findItem(long itemId, long userId) {
        LocalDateTime currentTime = LocalDateTime.now();
        Item item = itemRepository.findById(itemId).get();
        return addBookingInfoAndComments(item, userId, currentTime);
    }

    @Override
    public List<ItemDtoWithBookingDates> findAllByOwner(long ownerId) {
        LocalDateTime currentTime = LocalDateTime.now();
        List<Item> items = itemRepository.findItemsByOwnerId(ownerId);
        List<ItemDtoWithBookingDates> itemDtoWithBookingDates = new ArrayList<>();
        for (Item item : items) {
            itemDtoWithBookingDates.add(addBookingInfoAndComments(item, ownerId, currentTime));
        }
        return itemDtoWithBookingDates;
    }

    private ItemDtoWithBookingDates addBookingInfoAndComments(Item item, long userId, LocalDateTime currentTime) {
        List<CommentDto> comments = CommentMapper.toCommentDto(
                commentRepository.findAllByItemId(item.getId()));
        if (item.getOwnerId() == userId) {
            Booking lastBooking = bookingRepository.findFirstByItemIdAndEndBeforeOrderByEndDesc(item.getId(), currentTime);
            Booking nextBooking = bookingRepository.findFirstByItemIdAndStartAfterOrderByEndDesc(item.getId(), currentTime);
            return ItemMapper.toItemDtoWithBookingDates(item, lastBooking, nextBooking, comments);
        }
        return ItemMapper.toItemDtoWithoutBookingDates(item, comments);
    }

    @Override
    public List<ItemDto> searchItems(String text) {
        List<Item> items = new ArrayList<>();
        if (!text.isBlank()) {
            items = itemRepository.
                    findItemsByNameOrDescriptionContainingIgnoreCaseAndAvailableIsTrue(text, text);
        }
        return ItemMapper.toItemDto(items);
    }


    @Override
    public CommentDto addComment(long itemId, long userId, CommentDto commentDto) {
        LocalDateTime currentTime = LocalDateTime.now();
        Item item = itemRepository.findById(itemId).get();
        User author = userRepository.findById(userId).get();
        if (bookingRepository.findAllByBookerIdAndEndBefore(userId, LocalDateTime.now()).isEmpty()) {
            throw new ValidationException("У вас нет возможности оставить комментарий к этой вещи.");
        }
        Comment comment = commentRepository.save(CommentMapper.toCommentNew(commentDto, item, author, currentTime));
        return CommentMapper.toCommentDto(comment);
    }
}

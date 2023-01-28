package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestRepository;
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

    private final ItemRequestRepository itemRequestRepository;


    @Override
    @Transactional
    public ItemDto create(ItemDto itemDto, long ownerId) {
        if (!userRepository.existsById(ownerId)) {
            throw new NotFoundException("Пользователь не найден!");
        } else {
            Item item;
            if (itemDto.getRequestId() != null) {
                ItemRequest itemRequest = itemRequestRepository.findById(itemDto.getRequestId()).get();
                item = itemRepository.save(ItemMapper.toItemNew(itemDto, ownerId, itemRequest));
            } else {
                item = itemRepository.save(ItemMapper.toItemNew(itemDto, ownerId));
            }
            return ItemMapper.toItemDto(item);
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
                ItemRequest itemRequest = itemRequestRepository.findById(itemDto.getRequestId()).get();
                item.setRequest(itemRequest);
            }
            Item itemChanged = itemRepository.save(item);
            return ItemMapper.toItemDto(itemChanged);
        } else {
            throw new NotFoundException("Такая вещь у пользователя не найдена!");
        }
    }

    @Override
    @Transactional
    public ItemDtoBookingDatesAndComments findItem(long itemId, long userId) {
        LocalDateTime currentTime = LocalDateTime.now();
        Item item = itemRepository.findById(itemId).get();
        return addBookingInfoAndComments(item, userId, currentTime);
    }

    @Override
    @Transactional
    public List<ItemDtoBookingDatesAndComments> findAllByOwner(long ownerId, Pageable pageable) {
        LocalDateTime currentTime = LocalDateTime.now();
        Page<Item> pageItems = itemRepository.findItemsByOwnerId(ownerId, pageable);
        List<Item> items = pageItems.toList();
        List<ItemDtoBookingDatesAndComments> itemDtoBookingDateAndComments = new ArrayList<>();
        for (Item item : items) {
            itemDtoBookingDateAndComments.add(addBookingInfoAndComments(item, ownerId, currentTime));
        }
        return itemDtoBookingDateAndComments;
    }

    @Override
    @Transactional
    public List<ItemDto> searchItems(String text, Pageable pageable) {
        List<Item> items = new ArrayList<>();
        if (!text.isBlank()) {
            Page<Item> pageItems = itemRepository
                    .findItemsByNameOrDescriptionContainingIgnoreCaseAndAvailableIsTrue(text, text, pageable);
            items = pageItems.toList();
        }
        return ItemMapper.toItemDto(items);
    }

    @Override
    @Transactional
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

    private ItemDtoBookingDatesAndComments addBookingInfoAndComments(Item item, long userId, LocalDateTime currentTime) {
        List<Comment> comments = commentRepository.findAllByItemId(item.getId());
        if (item.getOwnerId() == userId) {
            Booking lastBooking = bookingRepository.findFirstByItemIdAndEndBeforeOrderByEndDesc(item.getId(), currentTime);
            Booking nextBooking = bookingRepository.findFirstByItemIdAndStartAfterOrderByEndDesc(item.getId(), currentTime);
            return ItemMapper.toItemDtoWithBookingDates(item, lastBooking, nextBooking, comments);
        }
        return ItemMapper.toItemDtoWithoutBookingDates(item, comments);
    }
}
package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.utils.exeptions.NotFoundException;
import ru.practicum.shareit.utils.exeptions.ValidationException;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;

    private final ItemService itemService;
    private final UserService userService;

    private final ItemRepository itemRepository;

    @Override
    @Transactional
    public BookingDtoToUser create(BookingDtoFromUser bookingDtoFromUser, long bookerId) {
        Item item = ItemMapper.toItem(itemService.findItem(bookingDtoFromUser.getItemId()));
        if (!userService.isUserExist(bookerId) || item.getOwnerId() == bookerId) {
            throw new NotFoundException("Некорректный запрос аренды");
        } else if (!itemService.isItemAvailable(bookingDtoFromUser.getItemId())
                || bookingDtoFromUser.getStart().isAfter(bookingDtoFromUser.getEnd())) {
            throw new ValidationException("Некорректный запрос аренды");
        }
        Booking booking = bookingRepository.save(
                BookingMapper.toBookingNew(bookingDtoFromUser, item, bookerId, BookingStatus.WAITING));
        return BookingMapper.toBookingDtoToUser(booking);
    }

    @Override
    @Transactional
    public BookingDtoToUser updateStatus(long ownerId, boolean approved, long bookingId) throws AccessDeniedException {
        Booking booking = bookingRepository.findById(bookingId).get();
        if (ownerId != itemService.findOwnerIdByItemId(booking.getItem().getId())) {
            throw new AccessDeniedException("Ошибка доступа!");
        }

        BookingStatus bookingStatus;
        if (approved) {
            bookingStatus = BookingStatus.APPROVED;
        } else {
            bookingStatus = BookingStatus.REJECTED;
        }

        if (booking.getStatus().equals(bookingStatus)) {
            throw new ValidationException("Такой статус уже установлен!");
        }

        booking.setStatus(bookingStatus);
        bookingRepository.save(booking);
        return BookingMapper.toBookingDtoToUser(
                bookingRepository.findById(bookingId).get());

        /*if (!bookingRepository.existsById(bookingId)) {
            throw new NotFoundException("Бронирование не найдено!");
        }
        if (ownerId != itemService.findOwnerIdByItemId(
                bookingRepository.findItemIdByBookingId(bookingId))) {
            throw new AccessDeniedException("Ошибка доступа!");
        }
        if (approved) {
            bookingRepository.updateStatusById(BookingStatus.APPROVED, bookingId);
        } else {
            bookingRepository.updateStatusById(BookingStatus.REJECTED, bookingId);
        }
        return BookingMapper.toBookingDtoToUser(
                bookingRepository.findById(bookingId).get());*/
    }

    @Override
    @Transactional
    public BookingDtoToUser findBooking(long userId, long bookingId) throws AccessDeniedException {
        Booking booking = bookingRepository.findById(bookingId).get();
        if (userId != booking.getBookerId()
                && userId != booking.getItem().getOwnerId()) {
            throw new AccessDeniedException("Ошибка доступа!");
        }
        return BookingMapper.toBookingDtoToUser(booking);
    }

    @Override
    @Transactional
    public List<BookingDtoToUser> findAllBookingsByBookerId(long bookerId, String state) {
        if (!userService.isUserExist(bookerId)) {
            throw new NotFoundException("Пользователь не существует!");
        }
        LocalDateTime currentTime = LocalDateTime.now();
        BookingState bookingState = getBookingState(state);
        switch (bookingState) {
            case ALL:
                return BookingMapper.toBookingDtoToUser(
                        bookingRepository.findAllByBookerIdOrderByStartDesc(bookerId));
            case CURRENT:
                return BookingMapper.toBookingDtoToUser(
                        bookingRepository.findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(bookerId, currentTime, currentTime)
                );
            case PAST:
                return BookingMapper.toBookingDtoToUser(
                        bookingRepository.findAllByBookerIdAndEndIsBeforeOrderByStartDesc(bookerId, currentTime));
            case FUTURE:
                return BookingMapper.toBookingDtoToUser(
                        bookingRepository.findAllByBookerIdAndStartIsAfterOrderByStartDesc(bookerId, currentTime));
            case WAITING:
                return BookingMapper.toBookingDtoToUser(
                        bookingRepository.findAllByBookerIdAndStatusEqualsOrderByStartDesc(bookerId, BookingStatus.WAITING));
            case REJECTED:
                return BookingMapper.toBookingDtoToUser(
                        bookingRepository.findAllByBookerIdAndStatusEqualsOrderByStartDesc(bookerId, BookingStatus.REJECTED));
            default:
                throw new ValidationException("Unknown state: UNSUPPORTED_STATUS");
        }
    }


    public List<BookingDtoToUser> findAllBookingsByItemOwner(long ownerId, String state) {
        if (!userService.isUserExist(ownerId)) {
            throw new NotFoundException("Пользователь не существует!");
        }
        LocalDateTime currentTime = LocalDateTime.now();
        BookingState bookingState = getBookingState(state);
        switch (bookingState) {
            case ALL:
                return BookingMapper.toBookingDtoToUser(
                        bookingRepository.findAllByItemOwnerIdOrderByStartDesc(ownerId));
            case CURRENT:
                return BookingMapper.toBookingDtoToUser(
                        bookingRepository.findAllByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(ownerId, currentTime, currentTime));
            case PAST:
                return BookingMapper.toBookingDtoToUser(
                        bookingRepository.findAllByItemOwnerIdAndEndIsBeforeOrderByStartDesc(ownerId, currentTime));
            case FUTURE:
                return BookingMapper.toBookingDtoToUser(
                        bookingRepository.findAllByItemOwnerIdAndStartIsAfterOrderByStartDesc(ownerId, currentTime));
            case WAITING:
                return BookingMapper.toBookingDtoToUser(
                        bookingRepository.findAllByItemOwnerIdAndStatusEqualsOrderByStartDesc(ownerId, BookingStatus.WAITING));
            case REJECTED:
                return BookingMapper.toBookingDtoToUser(
                        bookingRepository.findAllByItemOwnerIdAndStatusEqualsOrderByStartDesc(ownerId, BookingStatus.REJECTED));
            default:
                throw new ValidationException("Unknown state: UNSUPPORTED_STATUS");
        }
    }


    private BookingState getBookingState(String state) {
        try {
            return BookingState.valueOf(state);
        } catch (Exception e) {
            throw new ValidationException("Unknown state: UNSUPPORTED_STATUS");
        }
    }

}

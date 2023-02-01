package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.utils.exeptions.NotFoundException;
import ru.practicum.shareit.utils.exeptions.UnsupportedStateException;
import ru.practicum.shareit.utils.exeptions.ValidationException;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;

    private final ItemRepository itemRepository;

    private final UserRepository userRepository;


    @Override
    @Transactional
    public BookingDtoToUser create(BookingDtoFromUser bookingDtoFromUser, long bookerId) {
        Item item = itemRepository.findById(bookingDtoFromUser.getItemId()).get();
        if (!userRepository.existsById(bookerId) || item.getOwnerId() == bookerId) {
            throw new NotFoundException("Некорректный запрос аренды");
        }
        if (!item.getAvailable()
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
        if (ownerId != booking.getItem().getOwnerId()) {
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
    public List<BookingDtoToUser> findAllBookingsByBookerId(long bookerId, String state, Pageable pageable) {
        if (!userRepository.existsById(bookerId)) {
            throw new NotFoundException("Пользователь не существует!");
        }
        LocalDateTime currentTime = LocalDateTime.now();
        BookingState bookingState = getBookingState(state);
        Page<Booking> pageBooking;
        switch (bookingState) {
            case ALL:
                pageBooking = bookingRepository.findAllByBookerIdOrderByStartDesc(bookerId, pageable);
                return BookingMapper.toBookingDtoToUser(pageBooking.toList());
            case CURRENT:
                pageBooking = bookingRepository.findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(
                        bookerId, currentTime, currentTime, pageable);
                return BookingMapper.toBookingDtoToUser(pageBooking.toList());
            case PAST:
                pageBooking = bookingRepository.findAllByBookerIdAndEndIsBeforeOrderByStartDesc(
                        bookerId, currentTime, pageable);
                return BookingMapper.toBookingDtoToUser(pageBooking.toList());
            case FUTURE:
                pageBooking = bookingRepository.findAllByBookerIdAndStartIsAfterOrderByStartDesc(
                        bookerId, currentTime, pageable);
                return BookingMapper.toBookingDtoToUser(pageBooking.toList());
            case WAITING:
                pageBooking = bookingRepository.findAllByBookerIdAndStatusEqualsOrderByStartDesc(
                        bookerId, BookingStatus.WAITING, pageable);
                return BookingMapper.toBookingDtoToUser(pageBooking.toList());
            case REJECTED:
                pageBooking = bookingRepository.findAllByBookerIdAndStatusEqualsOrderByStartDesc(
                        bookerId, BookingStatus.REJECTED, pageable);
                return BookingMapper.toBookingDtoToUser(pageBooking.toList());
            default:
                throw new ValidationException("Unknown state: UNSUPPORTED_STATUS");
        }
    }


    @Override
    @Transactional
    public List<BookingDtoToUser> findAllBookingsByItemOwner(long ownerId, String state, Pageable pageable) {
        if (!userRepository.existsById(ownerId)) {
            throw new NotFoundException("Пользователь не существует!");
        }
        LocalDateTime currentTime = LocalDateTime.now();
        BookingState bookingState = getBookingState(state);
        Page<Booking> pageBooking;
        switch (bookingState) {
            case ALL:
                pageBooking = bookingRepository.findAllByItemOwnerIdOrderByStartDesc(ownerId, pageable);
                return BookingMapper.toBookingDtoToUser(pageBooking.toList());
            case CURRENT:
                pageBooking = bookingRepository.findAllByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(
                        ownerId, currentTime, currentTime, pageable);
                return BookingMapper.toBookingDtoToUser(pageBooking.toList());
            case PAST:
                pageBooking = bookingRepository.findAllByItemOwnerIdAndEndIsBeforeOrderByStartDesc(
                        ownerId, currentTime, pageable);
                return BookingMapper.toBookingDtoToUser(pageBooking.toList());
            case FUTURE:
                pageBooking = bookingRepository.findAllByItemOwnerIdAndStartIsAfterOrderByStartDesc(
                        ownerId, currentTime, pageable);
                return BookingMapper.toBookingDtoToUser(pageBooking.toList());
            case WAITING:
                pageBooking = bookingRepository.findAllByItemOwnerIdAndStatusEqualsOrderByStartDesc(
                        ownerId, BookingStatus.WAITING, pageable);
                return BookingMapper.toBookingDtoToUser(pageBooking.toList());
            case REJECTED:
                pageBooking = bookingRepository.findAllByItemOwnerIdAndStatusEqualsOrderByStartDesc(
                        ownerId, BookingStatus.REJECTED, pageable);
                return BookingMapper.toBookingDtoToUser(pageBooking.toList());
            default:
                throw new ValidationException("Unknown state: UNSUPPORTED_STATUS");
        }
    }

    private BookingState getBookingState(String state) {
        try {
            return BookingState.valueOf(state);
        } catch (Exception e) {
            throw new UnsupportedStateException("Unknown state: UNSUPPORTED_STATUS");
        }
    }
}
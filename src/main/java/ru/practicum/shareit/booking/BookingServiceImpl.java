package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.ItemDto;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.user.UserDto;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.utils.exeptions.NotFoundException;
import ru.practicum.shareit.utils.exeptions.ValidationException;

import java.nio.file.AccessDeniedException;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;

    private final ItemService itemService;
    private final UserService userService;

    @Override
    @Transactional
    public BookingDtoToUser create(BookingDtoFromUser bookingDtoFromUser, long bookerId) {
        if (!userService.isUserExist(bookerId)) {
            throw new NotFoundException("Некорректный запрос аренды");
        } else if (!itemService.isItemAvailable(bookingDtoFromUser.getItemId()) || bookingDtoFromUser.getStart().isAfter(bookingDtoFromUser.getEnd())) {
            throw new ValidationException("Некорректный запрос аренды");
        }
        Booking booking = bookingRepository.save(
                BookingMapper.toBooking(bookingDtoFromUser, bookerId, BookingStatus.WAITING));
        ItemDto itemDto = itemService.findItem(booking.getItemId());
        UserDto bookerDto = userService.findById(booking.getBookerId());
        return BookingMapper.toBookingDtoToUser(booking, itemDto, bookerDto);
    }

    @Override
    public BookingDtoToUser updateStatus(long ownerId, boolean approved, long bookingId) throws AccessDeniedException {
        if (!bookingRepository.existsById(bookingId)) {
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
        return findBookingById(bookingId);
    }

    public BookingDtoToUser findBookingById(long bookingId) {
        Booking booking = bookingRepository.findById(bookingId).get();
        ItemDto itemDto = itemService.findItem(booking.getItemId());
        UserDto bookerDto = userService.findById(booking.getBookerId());
        return BookingMapper.toBookingDtoToUser(booking, itemDto, bookerDto);
    }


}

package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.utils.exeptions.ValidationException;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;

    private final ItemService itemService;

    @Override
    @Transactional
    public BookingDto create(BookingDto bookingDto, long bookerId) {
        if (itemService.isItemAvailable(bookingDto.getItemId()) &&
        bookingDto.getStart().isBefore(bookingDto.getEnd())) {
            Booking booking = bookingRepository.save(BookingMapper.toBooking(bookingDto, bookerId, BookingStatus.WAITING));
            return BookingMapper.toBookingDto(booking);
        } else {
            throw new ValidationException("Некорректный запрос аренды");
        }

    }

}

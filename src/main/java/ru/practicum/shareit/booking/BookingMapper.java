package ru.practicum.shareit.booking;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.user.UserMapper;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BookingMapper {


    public static Booking toBookingNew(BookingDtoFromUser bookingDtoFromUser,
                                       Item item,
                                       Long bookerId,
                                       BookingStatus bookingStatus) {
        Booking booking = new Booking();
        booking.setStatus(bookingStatus);
        booking.setBookerId(bookerId);
        booking.setStart(bookingDtoFromUser.getStart());
        booking.setEnd(bookingDtoFromUser.getEnd());
        booking.setItem(item);
        return booking;
    }

    public static BookingDtoToUser toBookingDtoToUser(Booking booking) {
        BookingDtoToUser bookingDtoToUser = new BookingDtoToUser();
        bookingDtoToUser.setId(booking.getId());
        bookingDtoToUser.setStart(booking.getStart());
        bookingDtoToUser.setEnd(booking.getEnd());
        bookingDtoToUser.setStatus(booking.getStatus());
        bookingDtoToUser.setItem(ItemMapper.toItemDtoForBooking(booking.getItem()));
        bookingDtoToUser.setBooker(UserMapper.toUserDtoForBooking(booking.getBookerId()));
        return bookingDtoToUser;
    }

    public static List<BookingDtoToUser> toBookingDtoToUser(List<Booking> bookings) {
        List<BookingDtoToUser> dtos = new ArrayList<>();
        for (Booking booking : bookings) {
            dtos.add(toBookingDtoToUser(booking));
        }
        return dtos;
    }



}

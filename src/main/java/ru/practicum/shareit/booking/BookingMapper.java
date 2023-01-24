package ru.practicum.shareit.booking;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserDto;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BookingMapper {


    public static Booking toBooking(BookingDtoFromUser bookingDtoFromUser, long  bookerId, BookingStatus bookingStatus){
        Booking booking = new Booking();
        booking.setStatus(bookingStatus);
        booking.setBookerId(bookerId);
        booking.setStart(bookingDtoFromUser.getStart());
        booking.setEnd(bookingDtoFromUser.getEnd());
        booking.setItemId(bookingDtoFromUser.getItemId());
        return booking;
    }

    public static BookingDtoToUser toBookingDtoToUser(Booking booking, ItemDto itemDto, UserDto booker){
        BookingDtoToUser bookingDtoToUser = new BookingDtoToUser();
        bookingDtoToUser.setId(booking.getId());
        bookingDtoToUser.setStart(booking.getStart());
        bookingDtoToUser.setEnd(booking.getEnd());
        bookingDtoToUser.setStatus(booking.getStatus());
        bookingDtoToUser.setItem(itemDto);
        bookingDtoToUser.setBooker(booker);
        return bookingDtoToUser;
    }

    public static BookingDtoFromUser toBookingDto(Booking booking){
        BookingDtoFromUser bookingDtoFromUser = new BookingDtoFromUser();
        bookingDtoFromUser.setId(booking.getId());
        bookingDtoFromUser.setStart(booking.getStart());
        bookingDtoFromUser.setEnd(booking.getEnd());
        bookingDtoFromUser.setStatus(booking.getStatus());
        bookingDtoFromUser.setItemId(booking.getItemId());
        bookingDtoFromUser.setBookerId(booking.getBookerId());
        return bookingDtoFromUser;
    }

    public static List<BookingDtoFromUser> toBookingDto(List<Booking> bookings) {
        List<BookingDtoFromUser> dtos = new ArrayList<>();
        for (Booking booking : bookings) {
            dtos.add(toBookingDto(booking));
        }
        return dtos;
    }
}

package ru.practicum.shareit.booking;

public enum BookingState {

    // no filter
    ALL,

    // start_date < now && end_date > now
    CURRENT,

    // end_date < now
    PAST,

    // start_date > now
    FUTURE,

    //BookingStatus.WAITING
    WAITING,

    // BookingStatus.REJECTED
    REJECTED
}

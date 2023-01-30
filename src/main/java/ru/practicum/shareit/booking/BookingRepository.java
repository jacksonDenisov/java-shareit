package ru.practicum.shareit.booking;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    Page<Booking> findAllByBookerIdOrderByStartDesc(Long bookerId, Pageable pageable);

    Page<Booking> findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(
            Long bookerID, LocalDateTime start, LocalDateTime end, Pageable pageable);

    Page<Booking> findAllByBookerIdAndEndIsBeforeOrderByStartDesc(
            Long bookerId, LocalDateTime end, Pageable pageable);

    Page<Booking> findAllByBookerIdAndStartIsAfterOrderByStartDesc(
            Long bookerId, LocalDateTime start, Pageable pageable);

    Page<Booking> findAllByBookerIdAndStatusEqualsOrderByStartDesc(
            Long bookerId, BookingStatus bookingStatus, Pageable pageable);

    Page<Booking> findAllByItemOwnerIdOrderByStartDesc(Long ownerId, Pageable pageable);

    Page<Booking> findAllByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(
            Long ownerId, LocalDateTime start, LocalDateTime end, Pageable pageable);

    Page<Booking> findAllByItemOwnerIdAndEndIsBeforeOrderByStartDesc(
            Long ownerId, LocalDateTime end, Pageable pageable);

    Page<Booking> findAllByItemOwnerIdAndStartIsAfterOrderByStartDesc(
            Long ownerId, LocalDateTime start, Pageable pageable);

    Page<Booking> findAllByItemOwnerIdAndStatusEqualsOrderByStartDesc(
            Long ownerId, BookingStatus bookingStatus, Pageable pageable);

    Booking findFirstByItemIdAndEndBeforeOrderByEndDesc(Long itemId, LocalDateTime dateTime);

    Booking findFirstByItemIdAndStartAfterOrderByEndDesc(Long itemId, LocalDateTime dateTime);

    List<Booking> findAllByBookerIdAndEndBefore(long bookerId, LocalDateTime end);
}
package ru.practicum.shareit.booking;

import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findAllByBookerIdOrderByStartDesc(Long bookerId);

    List<Booking> findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(Long bookerID, LocalDateTime start, LocalDateTime end);

    List<Booking> findAllByBookerIdAndEndIsBeforeOrderByStartDesc(Long bookerId, LocalDateTime end);

    List<Booking> findAllByBookerIdAndStartIsAfterOrderByStartDesc(Long bookerId, LocalDateTime start);

    List<Booking> findAllByBookerIdAndStatusEqualsOrderByStartDesc(Long bookerId, BookingStatus bookingStatus);

    List<Booking> findAllByItemOwnerIdOrderByStartDesc (Long ownerId);

    List<Booking> findAllByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(Long ownerId, LocalDateTime start, LocalDateTime end);

    List<Booking> findAllByItemOwnerIdAndEndIsBeforeOrderByStartDesc(Long ownerId, LocalDateTime end);

    List<Booking> findAllByItemOwnerIdAndStartIsAfterOrderByStartDesc(Long ownerId, LocalDateTime start);

    List<Booking> findAllByItemOwnerIdAndStatusEqualsOrderByStartDesc(Long ownerId, BookingStatus bookingStatus);

    @Transactional
    @Modifying
    @Query("update Booking b set b.status = ?1 where b.id = ?2")
    int updateStatusById(BookingStatus status, Long id);

    @Query(value = "select b.item_id from bookings as b where b.id = ?1", nativeQuery = true)
    long findItemIdByBookingId(Long bookingId);

    Booking findAllByItemId(Long itemId);

}


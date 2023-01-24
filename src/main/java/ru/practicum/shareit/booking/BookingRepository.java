package ru.practicum.shareit.booking;

import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Transactional
    @Modifying
    @Query("update Booking b set b.status = ?1 where b.id = ?2")
    void updateStatusById(BookingStatus status, long id);

    @Query(value = "select b.item_id from bookings as b where b.id = ?1", nativeQuery = true)
    long findItemIdByBookingId(long bookingId);
}

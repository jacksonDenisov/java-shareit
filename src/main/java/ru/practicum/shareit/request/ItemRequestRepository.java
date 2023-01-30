package ru.practicum.shareit.request;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {

    @Query("SELECT ir FROM ItemRequest ir WHERE ir.requester.id = ?1 ORDER BY ir.created ASC")
    List<ItemRequest> findAllRequestsForOwner(Long requesterId);

    @Query("SELECT ir FROM ItemRequest ir WHERE ir.requester.id <> ?1")
    Page<ItemRequest> findAllRequestsOfOtherUsers(Long userId, Pageable pageable);
}
package ru.practicum.shareit.item;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    Page<Item> findItemsByOwnerIdOrderByIdAsc(Long ownerId, Pageable pageable);

    List<Item> findAllByRequestId(Long requestId);

    Page<Item> findItemsByNameOrDescriptionContainingIgnoreCaseAndAvailableIsTrue(
            String textName, String textDescription, Pageable pageable);

    @Query("SELECT i FROM Item i WHERE i.request.requester.id = ?1")
    List<Item> findAllItemsByRequesterId(long requesterId);

    @Query("SELECT i FROM Item i WHERE i.request.requester.id <> ?1")
    List<Item> findAllItemsOfOtherUsers(long requesterId);
}
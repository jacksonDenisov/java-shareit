package ru.practicum.shareit.item;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    Page<Item> findItemsByOwnerId(Long ownerId, Pageable pageable);

    List<Item> findAllByRequestId(Long requestId);

    Page<Item> findItemsByNameOrDescriptionContainingIgnoreCaseAndAvailableIsTrue(
            String textName, String textDescription, Pageable pageable);
}
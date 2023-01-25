package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findItemsByOwnerId(Long ownerId);

    List<Item> findItemsByNameOrDescriptionContainingIgnoreCaseAndAvailableIsTrue(String textName, String textDescription);

    @Query(value = "select it.is_available from items as it where it.id like ?1 " , nativeQuery = true)
    Boolean isItemAvailable(Long itemId);

    @Query(value = "select i.owner_id from items as i where i.id = ?1", nativeQuery = true)
    long findOwnerIdByItemId(Long itemId);

    @Override
    boolean existsById(Long aLong);

}
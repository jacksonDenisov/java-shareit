package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.model.Item;

public interface ItemRepository extends JpaRepository<Item, Long> {
    @Transactional
    @Modifying
    @Query("update Item i set i.name = ?1, i.description = ?2, i.available = ?3, i.requestId = ?4 " +
            "where i.id = ?5 and i.ownerId = ?6")
    int updateNameAndDescriptionAndAvailableAndRequestIdByIdAndOwnerId(String name,
                                                                       String description,
                                                                       Boolean available,
                                                                       long requestId,
                                                                       long id,
                                                                       long ownerId);

}

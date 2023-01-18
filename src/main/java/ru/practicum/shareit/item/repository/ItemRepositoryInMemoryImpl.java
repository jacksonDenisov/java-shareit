package ru.practicum.shareit.item.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.HashMap;
import java.util.Map;

@Repository
public class ItemRepositoryInMemoryImpl implements ItemRepository {

    private final Map<Long, Item> items = new HashMap<>();
    private static long id = 0;


    @Override
    public Item save(Item item){
        item.setId(++id);
        items.put(id, item);
        return item;
    }

}

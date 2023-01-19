package ru.practicum.shareit.item.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    @Override
    public Item update(ItemDto itemDto, long owner, long itemId){
        Item item = items.get(itemId);
        if (itemDto.getName() != null){
            item.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null){
            item.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null){
            item.setAvailable(itemDto.getAvailable());
        }
        return item;
    }

    @Override
    public Item findItem(long itemId){
        return items.get(itemId);
    }

    @Override
    public List<Item> findAll(){
        return new ArrayList<>(items.values());
    }

}

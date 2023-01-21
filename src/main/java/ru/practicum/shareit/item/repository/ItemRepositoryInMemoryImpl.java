package ru.practicum.shareit.item.repository;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class ItemRepositoryInMemoryImpl implements ItemRepository {

    private final Map<Long, Item> items;

    private final Map<Long, Long> itemsOwners;

    private long id;

    public ItemRepositoryInMemoryImpl() {
        items = new HashMap<>();
        itemsOwners = new HashMap<>();
        id = 0;
    }


    @Override
    public Item save(ItemDto itemDto, long owner) {
        Item item = ItemMapper.toItem(itemDto, ++id, owner);
        items.put(id, item);
        itemsOwners.put(id, owner);
        return item;
    }

    @Override
    public Item update(ItemDto itemDto, long owner, long itemId) {
        Item item = items.get(itemId);
        if (itemDto.getName() != null) {
            item.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            item.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }
        return item;
    }

    @Override
    public Item findItem(long itemId) {
        return items.get(itemId);
    }

    @Override
    public List<Item> findAll() {
        return new ArrayList<>(items.values());
    }

    @Override
    public List<Item> findAllByOwner(long owner) {
        return items.values()
                .stream()
                .filter(i -> i.getOwner() == owner)
                .collect(Collectors.toList());
    }

    @Override
    public List<Item> searchItems(String text) {
        List<Item> foundItems = new ArrayList<>();
        if (!text.isBlank()) {
            for (Item item : items.values()) {
                if ((StringUtils.containsIgnoreCase(item.getName(), text) ||
                        StringUtils.containsIgnoreCase(item.getDescription(), text)) &&
                        item.isAvailable()) {
                    foundItems.add(item);
                }
            }
        }
        return foundItems;
    }

    @Override
    public Map<Long, Long> getItemsOwners() {
        return itemsOwners;
    }

    @Override
    public boolean isItemExist(long itemId) {
        return items.get(itemId) != null;
    }
}

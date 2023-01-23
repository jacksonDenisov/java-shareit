package ru.practicum.shareit.in_memory_impl.item;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class ItemRepositoryInMemory {

    private final Map<Long, ItemForInMemoryImpl> items;

    private final Map<Long, Long> itemsOwners;

    private long id;

    public ItemRepositoryInMemory() {
        items = new HashMap<>();
        itemsOwners = new HashMap<>();
        id = 0;
    }


    public ItemForInMemoryImpl save(ItemDtoInMemory itemDtoInMemory, long owner) {
        ItemForInMemoryImpl itemForInMemoryImpl = ItemMapperForInMemoryImpl.toItem(itemDtoInMemory, ++id, owner);
        items.put(id, itemForInMemoryImpl);
        itemsOwners.put(id, owner);
        return itemForInMemoryImpl;
    }

    public ItemForInMemoryImpl update(ItemDtoInMemory itemDtoInMemory, long owner, long itemId) {
        ItemForInMemoryImpl itemForInMemoryImpl = items.get(itemId);
        if (itemDtoInMemory.getName() != null) {
            itemForInMemoryImpl.setName(itemDtoInMemory.getName());
        }
        if (itemDtoInMemory.getDescription() != null) {
            itemForInMemoryImpl.setDescription(itemDtoInMemory.getDescription());
        }
        if (itemDtoInMemory.getAvailable() != null) {
            itemForInMemoryImpl.setAvailable(itemDtoInMemory.getAvailable());
        }
        return itemForInMemoryImpl;
    }

    public ItemForInMemoryImpl findItem(long itemId) {
        return items.get(itemId);
    }

    public List<ItemForInMemoryImpl> findAll() {
        return new ArrayList<>(items.values());
    }

    public List<ItemForInMemoryImpl> findAllByOwner(long owner) {
        return items.values()
                .stream()
                .filter(i -> i.getOwner() == owner)
                .collect(Collectors.toList());
    }

    public List<ItemForInMemoryImpl> searchItems(String text) {
        List<ItemForInMemoryImpl> foundItemForInMemoryImpls = new ArrayList<>();
        if (!text.isBlank()) {
            for (ItemForInMemoryImpl itemForInMemoryImpl : items.values()) {
                if ((StringUtils.containsIgnoreCase(itemForInMemoryImpl.getName(), text) ||
                        StringUtils.containsIgnoreCase(itemForInMemoryImpl.getDescription(), text)) &&
                        itemForInMemoryImpl.isAvailable()) {
                    foundItemForInMemoryImpls.add(itemForInMemoryImpl);
                }
            }
        }
        return foundItemForInMemoryImpls;
    }

    public Map<Long, Long> getItemsOwners() {
        return itemsOwners;
    }

    public boolean isItemExist(long itemId) {
        return items.get(itemId) != null;
    }
}

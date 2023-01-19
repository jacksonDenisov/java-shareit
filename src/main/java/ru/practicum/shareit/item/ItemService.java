package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exeptions.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepositoryInMemoryImpl;
import ru.practicum.shareit.user.UserService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ItemService {

    private ItemRepositoryInMemoryImpl repository;
    private UserService userService;

    private final Map<Long, Long> itemsOwners = new HashMap<>();


    @Autowired
    public ItemService(ItemRepositoryInMemoryImpl repository, UserService userService) {
        this.repository = repository;
        this.userService = userService;
    }


    public Item create(ItemDto itemDto, long owner) {
        log.info("Создаем новую вещь " + itemDto);
        if (!userService.isUserExist(owner)) {
            throw new NotFoundException("Пользователь не найден!");
        }
        Item item = repository.save(ItemMapper.toItem(itemDto, owner));
        itemsOwners.put(item.getId(), owner);
        return item;
    }

    public Item update(ItemDto itemDto, long owner, long itemId) {
        if (!userService.isUserExist(owner)) {
            throw new NotFoundException("Пользователь не найден!");
        } else if (!itemsOwners.get(itemId).equals(owner)) {
            throw new NotFoundException("Такая вещь у пользователя не найдена!");
        }
        return repository.update(itemDto, owner, itemId);
    }

    public Item findItem(long itemId) {
        return repository.findItem(itemId);
    }

    public List<Item> findAllBNyOwner(long owner) {
        return repository
                .findAll()
                .stream()
                .filter(i -> i.getOwner() == owner)
                .collect(Collectors.toList());
    }


    public List<Item> searchItems(String text) {
        List<Item> foundItems = new ArrayList<>();
        if (text.isBlank()) {
            return foundItems;
        }
        for (Item item : repository.findAll()) {
            if (StringUtils.containsIgnoreCase(item.getName(), text) ||
                    StringUtils.containsIgnoreCase(item.getDescription(), text)) {
                foundItems.add(item);
            }
        }
        return foundItems.stream().filter(i -> i.isAvailable()).collect(Collectors.toList());
    }
}

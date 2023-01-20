package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepositoryInMemoryImpl;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.utils.exeptions.NotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ItemService {

    private final ItemRepositoryInMemoryImpl repository;

    private final UserService userService;


    @Autowired
    public ItemService(ItemRepositoryInMemoryImpl repository, UserService userService) {
        this.repository = repository;
        this.userService = userService;
    }


    public Item create(ItemDto itemDto, long owner) {
        log.info("Создаем новую вещь {}.", itemDto);
        if (!userService.isUserExist(owner)) {
            throw new NotFoundException("Пользователь не найден!");
        }
        return repository.save(itemDto, owner);
    }

    public Item update(ItemDto itemDto, long owner, long itemId) {
        log.info("Обновляем вещь с id = {}.", itemId);
        if (!userService.isUserExist(owner)) {
            throw new NotFoundException("Пользователь не найден!");
        }
        if (!repository.getItemsOwners().get(itemId).equals(owner)) {
            throw new NotFoundException("Такая вещь у пользователя не найдена!");
        }
        return repository.update(itemDto, owner, itemId);
    }

    public Item findItem(long itemId) {
        log.info("Возвращаем вещь с id = {}.", itemId);
        if (!repository.isItemExist(itemId)) {
            throw new NotFoundException("Такая вещь не найдена!");
        }
        return repository.findItem(itemId);
    }

    public List<Item> findAllBNyOwner(long owner) {
        log.info("Возвращаем список вещей пользователя с id= {}.", owner);
        if (!userService.isUserExist(owner)) {
            throw new NotFoundException("Пользователь не найден!");
        }
        return repository
                .findAll()
                .stream()
                .filter(i -> i.getOwner() == owner)
                .collect(Collectors.toList());
    }


    public List<Item> searchItems(String text) {
        log.info("Выполняем поиск вещей по запросу: {}.", text);
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
        return foundItems
                .stream()
                .filter(Item::isAvailable)
                .collect(Collectors.toList());
    }
}

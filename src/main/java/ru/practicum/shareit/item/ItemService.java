package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.repository.ItemRepositoryInMemoryImpl;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.utils.exeptions.NotFoundException;

import java.util.List;

@Service
@Slf4j
public class ItemService {

    private final ItemRepository itemRepository;

    private final UserService userService;


    @Autowired
    public ItemService(ItemRepositoryInMemoryImpl itemRepositoryInMemoryImpl, UserService userService) {
        this.itemRepository = itemRepositoryInMemoryImpl;
        this.userService = userService;
    }


    public Item create(ItemDto itemDto, long owner) {
        log.info("Создаем новую вещь {}.", itemDto);
        if (!userService.isUserExist(owner)) {
            throw new NotFoundException("Пользователь не найден!");
        }
        return itemRepository.save(itemDto, owner);
    }

    public Item update(ItemDto itemDto, long owner, long itemId) {
        log.info("Обновляем вещь с id = {}.", itemId);
        if (!userService.isUserExist(owner)) {
            throw new NotFoundException("Пользователь не найден!");
        }
        if (!itemRepository.getItemsOwners().get(itemId).equals(owner)) {
            throw new NotFoundException("Такая вещь у пользователя не найдена!");
        }
        return itemRepository.update(itemDto, owner, itemId);
    }

    public Item findItem(long itemId) {
        log.info("Возвращаем вещь с id = {}.", itemId);
        if (!itemRepository.isItemExist(itemId)) {
            throw new NotFoundException("Такая вещь не найдена!");
        }
        return itemRepository.findItem(itemId);
    }

    public List<Item> findAllByOwner(long owner) {
        log.info("Возвращаем список вещей пользователя с id= {}.", owner);
        if (!userService.isUserExist(owner)) {
            throw new NotFoundException("Пользователь не найден!");
        }
        return itemRepository.findAllByOwner(owner);
    }


    public List<Item> searchItems(String text) {
        log.info("Выполняем поиск вещей по запросу: {}.", text);
        return itemRepository.searchItems(text);
    }
}

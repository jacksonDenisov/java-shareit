package ru.practicum.shareit.in_memory_impl.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.in_memory_impl.user.UserServiceInMemoryInMemoryImpl;
import ru.practicum.shareit.utils.exeptions.NotFoundException;

import java.util.List;

@Service
@Slf4j
@Qualifier("ItemServiceInMemory")
public class ItemServiceInMemoryInMemoryImpl implements ItemServiceInMemory {

    private final ItemRepositoryInMemory itemRepositoryInMemory;

    private final UserServiceInMemoryInMemoryImpl userServiceInMemoryImpl;


    @Autowired
    public ItemServiceInMemoryInMemoryImpl(ItemRepositoryInMemory itemRepositoryInMemory, UserServiceInMemoryInMemoryImpl userServiceInMemoryImpl) {
        this.itemRepositoryInMemory = itemRepositoryInMemory;
        this.userServiceInMemoryImpl = userServiceInMemoryImpl;
    }


    @Override
    public ItemForInMemoryImpl create(ItemDtoInMemory itemDtoInMemory, long owner) {
        log.info("Создаем новую вещь {}.", itemDtoInMemory);
        if (!userServiceInMemoryImpl.isUserExist(owner)) {
            throw new NotFoundException("Пользователь не найден!");
        }
        return itemRepositoryInMemory.save(itemDtoInMemory, owner);
    }

    @Override
    public ItemForInMemoryImpl update(ItemDtoInMemory itemDtoInMemory, long owner, long itemId) {
        log.info("Обновляем вещь с id = {}.", itemId);
        if (!userServiceInMemoryImpl.isUserExist(owner)) {
            throw new NotFoundException("Пользователь не найден!");
        }
        if (!itemRepositoryInMemory.getItemsOwners().get(itemId).equals(owner)) {
            throw new NotFoundException("Такая вещь у пользователя не найдена!");
        }
        return itemRepositoryInMemory.update(itemDtoInMemory, owner, itemId);
    }

    @Override
    public ItemForInMemoryImpl findItem(long itemId) {
        log.info("Возвращаем вещь с id = {}.", itemId);
        if (!itemRepositoryInMemory.isItemExist(itemId)) {
            throw new NotFoundException("Такая вещь не найдена!");
        }
        return itemRepositoryInMemory.findItem(itemId);
    }

    @Override
    public List<ItemForInMemoryImpl> findAllByOwner(long owner) {
        log.info("Возвращаем список вещей пользователя с id= {}.", owner);
        if (!userServiceInMemoryImpl.isUserExist(owner)) {
            throw new NotFoundException("Пользователь не найден!");
        }
        return itemRepositoryInMemory.findAllByOwner(owner);
    }

    @Override
    public List<ItemForInMemoryImpl> searchItems(String text) {
        log.info("Выполняем поиск вещей по запросу: {}.", text);
        return itemRepositoryInMemory.searchItems(text);
    }
}

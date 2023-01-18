package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepositoryInMemoryImpl;
import ru.practicum.shareit.user.UserService;

@Service
@Slf4j
public class ItemService {

    private ItemRepositoryInMemoryImpl repository;


    @Autowired
    public ItemService(ItemRepositoryInMemoryImpl repository){
        this.repository = repository;
    }


    public Item create(ItemDto itemDto, long owner) {
        log.info("Создаем новую вещь " + itemDto);

        return repository.save(ItemMapper.toItem(itemDto, owner));
    }


}

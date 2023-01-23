package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {


    private final ItemRepository itemRepository;

    @Override
    @Transactional
    public ItemDto create(ItemDto itemDto, long ownerId) {
        Item item = itemRepository.save(ItemMapper.toItem(itemDto, ownerId));
        return ItemMapper.toItemDto(item);
    }

    @Override
    public ItemDto update(ItemDto itemDto, long ownerId, long itemId) {
        ItemDto itemDto1 = findItem(itemId);


        return findItem(itemId);
    }

    @Override
    public ItemDto findItem(long itemId) {
        Optional<Item> item = itemRepository.findById(itemId);
        return ItemMapper.toItemDto(item.get());
    }

    @Override
    public List<ItemDto> findAllByOwner(long ownerId) {
        List<Item> items = itemRepository.findAll();
        return ItemMapper.toItemDto(items);
    }

    //public List<ItemDto> searchItems(String text);
}

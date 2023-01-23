package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.utils.exeptions.NotFoundException;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {


    private final ItemRepository itemRepository;
    private final UserService userService;

    @Override
    @Transactional
    public ItemDto create(ItemDto itemDto, long ownerId) {
        if (userService.isUserExist(ownerId)) {
            Item item = itemRepository.save(ItemMapper.toItem(itemDto, ownerId));
            return ItemMapper.toItemDto(item);
        } else {
            throw new NotFoundException("Пользователь не найден!");
        }
    }

    @Override
    @Transactional
    public ItemDto update(ItemDto itemDto, long ownerId, long itemId) throws AccessDeniedException {
        Item item = itemRepository.findById(itemId).get();
        if (item.getOwnerId() != null && item.getOwnerId().equals(ownerId)) {
            if (itemDto.getName() != null) {
                item.setName(itemDto.getName());
            }
            if (itemDto.getDescription() != null) {
                item.setDescription(itemDto.getDescription());
            }
            if (itemDto.getAvailable() != null) {
                item.setAvailable(itemDto.getAvailable());
            }
            if (itemDto.getRequestId() != null) {
                item.setRequestId(itemDto.getRequestId());
            }
            Item itemChanged = itemRepository.save(item);
            return ItemMapper.toItemDto(itemChanged);
        } else {
            throw new NotFoundException("Такая вещь у пользователя не найдена!");
        }
    }

    @Override
    public ItemDto findItem(long itemId) {
        Optional<Item> item = itemRepository.findById(itemId);
        return ItemMapper.toItemDto(item.get());
    }

    @Override
    public List<ItemDto> findAllByOwner(long ownerId) {
        List<Item> items = itemRepository.findItemsByOwnerId(ownerId);
        return ItemMapper.toItemDto(items);
    }

    //public List<ItemDto> searchItems(String text);
}

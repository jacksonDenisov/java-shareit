package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.utils.exeptions.NotFoundException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestRepository itemRequestRepository;

    private final UserRepository userRepository;

    private final ItemRepository itemRepository;

    @Override
    @Transactional
    public ItemRequestDto create(long requesterId, ItemRequestDto itemRequestDto) {
        User user = userRepository.findById(requesterId).get();
        LocalDateTime created = LocalDateTime.now();
        ItemRequest itemRequest = ItemRequestMapper.toItemRequest(user, itemRequestDto, created);
        return ItemRequestMapper.toItemRequestDto(itemRequestRepository.save(itemRequest));
    }

    @Override
    @Transactional
    public List<ItemRequestDtoWithReply> findAllByOwner(long ownerId) {
        if (!userRepository.existsById(ownerId)) {
            throw new NotFoundException("Пользователь не найден!");
        }
        List<ItemRequest> itemRequests = itemRequestRepository.findAllRequestsForOwner(ownerId);
        List<Item> items = itemRepository.findAllItemsByRequesterId(ownerId);
        return fillItemRequestsWithItems(itemRequests, items);
    }

    @Override
    @Transactional
    public List<ItemRequestDtoWithReply> findAllForOneUser(long userId, Pageable pageable) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователь не найден!");
        }
        Page<ItemRequest> pageItemRequests = itemRequestRepository.findAllRequestsOfOtherUsers(userId, pageable);
        List<ItemRequest> itemRequests = pageItemRequests.toList();
        List<Item> items = itemRepository.findAllItemsOfOtherUsers(userId);
        return fillItemRequestsWithItems(itemRequests, items);
    }

    @Override
    @Transactional
    public ItemRequestDtoWithReply findById(long requestId, long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователь не найден!");
        }
        ItemRequest itemRequest = itemRequestRepository.findById(requestId).get();
        List<Item> items = itemRepository.findAllByRequestId(requestId);
        return ItemRequestMapper.toItemRequestDtoWithReply(itemRequest, items);
    }

    private List<ItemRequestDtoWithReply> fillItemRequestsWithItems(List<ItemRequest> itemRequests, List<Item> items) {
        List<ItemRequestDtoWithReply> itemRequestDtoWithReplies = new ArrayList<>();
        for (ItemRequest itemRequest : itemRequests) {
            List<Item> itemsForRequest = new ArrayList<>();
            for (Item item : items) {
                if (item.getRequest().getId() == itemRequest.getId()) {
                    itemsForRequest.add(item);
                }
            }
            itemRequestDtoWithReplies.add(ItemRequestMapper.toItemRequestDtoWithReply(itemRequest, itemsForRequest));
        }
        return itemRequestDtoWithReplies;
    }
}
package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;

    public ItemRequestDtoToUser create(long requesterId, ItemRequestDtoFromUser itemRequestDtoFromUser) {
        User user = userRepository.findById(requesterId).get();
        LocalDateTime created = LocalDateTime.now();
        ItemRequest itemRequest = ItemRequestMapper.toItemRequest(user, itemRequestDtoFromUser, created);
        return ItemRequestMapper.toItemRequestDtoToUser(itemRequestRepository.save(itemRequest));
    }

    public List<ItemRequestDtoToUser> findAllByOwner(long ownerId) {
        return new ArrayList<>();
    }


    public List<ItemRequestDtoToUser> findAll() {
        return new ArrayList<>();
    }

    public ItemRequestDtoToUser findById(long requestId) {
        return new ItemRequestDtoToUser();
    }
}

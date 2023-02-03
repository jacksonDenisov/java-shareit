package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {

    private final ItemRequestService itemRequestService;

    private static final String USER_ID_REQUEST_HEADER = "X-Sharer-User-Id";

    @PostMapping
    protected ItemRequestDto create(@RequestHeader(USER_ID_REQUEST_HEADER) long requesterId,
                                    @RequestBody ItemRequestDto itemRequestDto) {
        return itemRequestService.create(requesterId, itemRequestDto);
    }

    @GetMapping
    protected List<ItemRequestDtoWithReply> findAllByOwner(@RequestHeader(USER_ID_REQUEST_HEADER) long ownerId) {
        return itemRequestService.findAllByOwner(ownerId);
    }

    @GetMapping("/all")
    protected List<ItemRequestDtoWithReply> findAllForOneUser(
            @RequestHeader(USER_ID_REQUEST_HEADER) long userId,
            @RequestParam(name = "from", required = false) int from,
            @RequestParam(name = "size", required = false) int size) {
        Sort sortByCreated = Sort.by(Sort.Direction.ASC, "created");
        Pageable pageable = PageRequest.of(from / size, size, sortByCreated);
        return itemRequestService.findAllForOneUser(userId, pageable);
    }

    @GetMapping("/{requestId}")
    protected ItemRequestDtoWithReply findById(@RequestHeader(USER_ID_REQUEST_HEADER) long userId,
                                               @PathVariable long requestId) {
        return itemRequestService.findById(requestId, userId);
    }
}
package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {

    private final ItemRequestService itemRequestService;

    @PostMapping
    protected ItemRequestDtoToUser create(@RequestHeader("X-Sharer-User-Id") long requesterId,
                                          @Valid @RequestBody ItemRequestDtoFromUser itemRequestDtoFromUser) {
        return itemRequestService.create(requesterId, itemRequestDtoFromUser);
    }

    @GetMapping
    protected List<ItemRequestDtoToUser> findAllByOwner(@RequestHeader("X-Sharer-User-Id") long ownerId) {
        return itemRequestService.findAllByOwner(ownerId);
    }

    @GetMapping("/all")
    protected List<ItemRequestDtoToUser> findAll(@RequestHeader("X-Sharer-User-Id") long userId,
                                                 @RequestParam(name = "from", defaultValue = "0") long from,
                                                 @RequestParam(name = "size", defaultValue = "0") long size) {
        return itemRequestService.findAll();
    }

    @GetMapping("/{requestId}")
    protected ItemRequestDtoToUser findById(@RequestHeader("X-Sharer-User-Id") long userId,
                                            @PathVariable long requestId) {
        return itemRequestService.findById(requestId);
    }
}

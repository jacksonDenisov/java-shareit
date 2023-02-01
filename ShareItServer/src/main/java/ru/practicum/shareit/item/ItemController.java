package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    private static final String USER_ID_REQUEST_HEADER = "X-Sharer-User-Id";


    @PostMapping
    protected ItemDto create(@RequestBody ItemDto itemDto,
                             @RequestHeader(USER_ID_REQUEST_HEADER) long ownerId) {
        return itemService.create(itemDto, ownerId);
    }

    @PatchMapping("/{itemId}")
    protected ItemDto update(@RequestBody ItemDto itemDto,
                             @RequestHeader(USER_ID_REQUEST_HEADER) long ownerId,
                             @PathVariable("itemId") long itemId) throws AccessDeniedException {
        return itemService.update(itemDto, ownerId, itemId);
    }

    @GetMapping("/{itemId}")
    protected ItemDtoBookingDatesAndComments findItem(@RequestHeader(USER_ID_REQUEST_HEADER) long userId,
                                                      @PathVariable("itemId") long itemId) {
        return itemService.findItem(itemId, userId);
    }

    @GetMapping
    protected List<ItemDtoBookingDatesAndComments> findAllByOwner(
            @RequestHeader(USER_ID_REQUEST_HEADER) long ownerId,
            @RequestParam(name = "from", required = false, defaultValue = "1") int from,
            @RequestParam(name = "size", required = false, defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(from / size, size);
        return itemService.findAllByOwner(ownerId, pageable);
    }

    @GetMapping("/search")
    protected List<ItemDto> searchItems(
            @RequestParam("text") String search,
            @RequestParam(name = "from", required = false, defaultValue = "1") int from,
            @RequestParam(name = "size", required = false, defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(from / size, size);
        return itemService.searchItems(search, pageable);
    }

    @PostMapping("/{itemId}/comment")
    protected CommentDto addComment(@RequestHeader(USER_ID_REQUEST_HEADER) long userId,
                                    @PathVariable("itemId") long itemId,
                                    @RequestBody CommentDto commentDto) {
        return itemService.addComment(itemId, userId, commentDto);
    }
}
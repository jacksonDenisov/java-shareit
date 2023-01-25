package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping
    protected ItemDto create(@Valid @RequestBody ItemDto itemDto,
                             @RequestHeader("X-Sharer-User-Id") long ownerId) {
        return itemService.create(itemDto, ownerId);
    }

    @PatchMapping("/{itemId}")
    protected ItemDto update(@RequestBody ItemDto itemDto,
                             @RequestHeader("X-Sharer-User-Id") long ownerId,
                             @PathVariable("itemId") long itemId) throws AccessDeniedException {
        return itemService.update(itemDto, ownerId, itemId);
    }

    @GetMapping("/{itemId}")
    protected ItemDtoWithBookingDates findItem(@PathVariable("itemId") long itemId,
                                               @RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.findItem(itemId, userId);
    }

    @GetMapping
    protected List<ItemDtoWithBookingDates> findAll(@RequestHeader("X-Sharer-User-Id") long ownerId) {
        return itemService.findAllByOwner(ownerId);
    }

    @GetMapping("/search")
    protected List<ItemDto> searchItems(@RequestParam("text") String search) {
        return itemService.searchItems(search);
    }

    @PostMapping("/{itemId}/comment")
    protected CommentDto addComment(@PathVariable("itemId") long itemId,
                                    @RequestHeader("X-Sharer-User-Id") long userId,
                                    @Valid @RequestBody CommentDto commentDto) {
        return itemService.addComment(itemId, userId, commentDto);
    }
}

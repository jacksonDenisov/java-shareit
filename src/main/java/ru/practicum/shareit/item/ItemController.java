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
    protected ItemDto findItem(@PathVariable("itemId") long itemId) {
        return itemService.findItem(itemId);
    }

    @GetMapping
    protected List<ItemDto> findAll(@RequestHeader("X-Sharer-User-Id") long ownerId) {
        return itemService.findAllByOwner(ownerId);
    }

    @GetMapping("/search")
    protected List<ItemDto> searchItems(@RequestParam("text") String search) {
        return itemService.searchItems(search);
    }
}

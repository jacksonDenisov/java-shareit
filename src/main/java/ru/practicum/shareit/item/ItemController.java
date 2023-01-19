package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
public class ItemController {

    private ItemService service;

    @Autowired
    public ItemController(ItemService service) {
        this.service = service;
    }

    @PostMapping
    protected Item create(@Valid @RequestBody ItemDto itemDto, @RequestHeader("X-Sharer-User-Id") long owner) {
        return service.create(itemDto, owner);
    }

    @PatchMapping("/{itemId}")
    protected Item update(@RequestBody ItemDto itemDto,
                          @RequestHeader("X-Sharer-User-Id") long owner,
                          @PathVariable("itemId") long itemId){
        return service.update(itemDto, owner, itemId);
    }
    @GetMapping("/{itemId}")
    protected Item findItem(@PathVariable("itemId") long itemId){
        return service.findItem(itemId);
    }

    @GetMapping
    protected List<Item> findAll(@RequestHeader("X-Sharer-User-Id") long owner){
        return service.findAllBNyOwner(owner);
    }

    @GetMapping("/search")
    protected List<Item> searchItems(@RequestParam("text") String search){
        return service.searchItems(search);
    }
}

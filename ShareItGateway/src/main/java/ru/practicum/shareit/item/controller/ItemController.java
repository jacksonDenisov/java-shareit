package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Range;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.client.ItemClient;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RestController
@RequestMapping("/items")
@Validated
@RequiredArgsConstructor
public class ItemController {

    private final ItemClient itemClient;

    private static final String USER_ID_REQUEST_HEADER = "X-Sharer-User-Id";


    @PostMapping
    protected ResponseEntity<Object> create(@Valid @RequestBody ItemDto itemDto,
                                            @RequestHeader(USER_ID_REQUEST_HEADER) long ownerId) {
        return itemClient.create(itemDto, ownerId);
    }

    @PatchMapping("/{itemId}")
    protected ResponseEntity<Object> update(@RequestBody ItemDto itemDto,
                                            @RequestHeader(USER_ID_REQUEST_HEADER) long ownerId,
                                            @PathVariable("itemId") long itemId) {
        return itemClient.update(itemDto, ownerId, itemId);
    }

    @GetMapping("/{itemId}")
    protected ResponseEntity<Object> findItem(@RequestHeader(USER_ID_REQUEST_HEADER) long userId,
                                              @PathVariable("itemId") long itemId) {
        return itemClient.findItem(itemId, userId);
    }

    @GetMapping
    protected ResponseEntity<Object> findAllByOwner(
            @RequestHeader(USER_ID_REQUEST_HEADER) long ownerId,
            @RequestParam(name = "from", required = false, defaultValue = "1")
            @Range(message = "Значение from должно быть больше нуля") int from,
            @RequestParam(name = "size", required = false, defaultValue = "20") @Positive int size) {
        return itemClient.findAllByOwner(ownerId, from, size);
    }

    @GetMapping("/search")
    protected ResponseEntity<Object> searchItems(
            @RequestParam("text") String search,
            @RequestParam(name = "from", required = false, defaultValue = "1")
            @Range(message = "Значение from должно быть больше нуля") int from,
            @RequestParam(name = "size", required = false, defaultValue = "20") @Positive int size) {
        return itemClient.searchItems(search, from, size);
    }

    @PostMapping("/{itemId}/comment")
    protected ResponseEntity<Object> addComment(@RequestHeader(USER_ID_REQUEST_HEADER) long userId,
                                                @PathVariable("itemId") long itemId,
                                                @Valid @RequestBody CommentDto commentDto) {
        return itemClient.addComment(itemId, userId, commentDto);
    }
}
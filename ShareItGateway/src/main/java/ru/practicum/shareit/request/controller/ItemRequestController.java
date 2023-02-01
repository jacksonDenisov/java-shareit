package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Range;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.client.ItemRequestClient;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
@Validated
public class ItemRequestController {

    private final ItemRequestClient itemRequestClient;

    private static final String USER_ID_REQUEST_HEADER = "X-Sharer-User-Id";

    @PostMapping
    protected ResponseEntity<Object> create(@RequestHeader(USER_ID_REQUEST_HEADER) long requesterId,
                                            @Valid @RequestBody ItemRequestDto itemRequestDto) {
        return itemRequestClient.create(requesterId, itemRequestDto);
    }

    @GetMapping
    protected ResponseEntity<Object> findAllByOwner(@RequestHeader(USER_ID_REQUEST_HEADER) long ownerId) {
        return itemRequestClient.findAllByOwner(ownerId);
    }

    @GetMapping("/all")
    protected ResponseEntity<Object> findAllForOneUser(
            @RequestHeader(USER_ID_REQUEST_HEADER) long userId,
            @RequestParam(name = "from", required = false, defaultValue = "1")
            @Range(message = "Значение from должно быть больше нуля") int from,
            @RequestParam(name = "size", required = false, defaultValue = "20") @Positive int size) {
        return itemRequestClient.findAllForOneUser(userId, from, size);
    }

    @GetMapping("/{requestId}")
    protected ResponseEntity<Object> findById(@RequestHeader(USER_ID_REQUEST_HEADER) long userId,
                                              @PathVariable long requestId) {
        return itemRequestClient.findById(requestId, userId);
    }
}
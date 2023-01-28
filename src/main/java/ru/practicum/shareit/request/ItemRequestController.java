package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Range;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
@Validated
public class ItemRequestController {

    private final ItemRequestService itemRequestService;

    private static final String USER_ID_REQUEST_HEADER = "X-Sharer-User-Id";

    @PostMapping
    protected ItemRequestDto create(@RequestHeader(USER_ID_REQUEST_HEADER) long requesterId,
                                    @Valid @RequestBody ItemRequestDto itemRequestDto) {
        return itemRequestService.create(requesterId, itemRequestDto);
    }

    @GetMapping
    protected List<ItemRequestDtoWithReply> findAllByOwner(@RequestHeader(USER_ID_REQUEST_HEADER) long ownerId) {
        return itemRequestService.findAllByOwner(ownerId);
    }

    @GetMapping("/all")
    protected List<ItemRequestDtoWithReply> findAllForOneUser(
            @RequestHeader(USER_ID_REQUEST_HEADER) long userId,
            @RequestParam(name = "from", required = false, defaultValue = "1")
            @Range(message = "Значение from должно быть больше нуля") int from,
            @RequestParam(name = "size", required = false, defaultValue = "20") @Positive int size) {
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
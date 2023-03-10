package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {

    private final UserService userService;


    @PostMapping
    protected UserDto create(@RequestBody UserDto userDto) {
        return userService.create(userDto);
    }

    @PatchMapping("/{userId}")
    protected UserDto update(@RequestBody UserDto userDto, @PathVariable long userId) {
        return userService.update(userDto, userId);
    }

    @GetMapping("/{userId}")
    protected UserDto findById(@PathVariable long userId) {
        return userService.findById(userId);
    }

    @GetMapping
    protected List<UserDto> findAll() {
        return userService.findAll();
    }

    @DeleteMapping("/{userId}")
    protected boolean deleteById(@PathVariable long userId) {
        return userService.deleteById(userId);
    }
}
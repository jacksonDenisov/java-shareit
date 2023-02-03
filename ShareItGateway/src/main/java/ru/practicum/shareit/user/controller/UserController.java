package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.client.UserClient;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.Valid;

@Controller
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserController {

    private final UserClient userClient;

    @PostMapping
    protected ResponseEntity<Object> create(@Valid @RequestBody UserDto userDto) {
        return userClient.create(userDto);
    }

    @PatchMapping("/{userId}")
    protected ResponseEntity<Object> update(@RequestBody UserDto userDto, @PathVariable long userId) {
        return userClient.update(userDto, userId);
    }

    @GetMapping("/{userId}")
    protected ResponseEntity<Object> findById(@PathVariable long userId) {
        return userClient.findById(userId);
    }

    @GetMapping
    protected ResponseEntity<Object> findAll() {
        return userClient.findAll();
    }

    @DeleteMapping("/{userId}")
    protected ResponseEntity<Object> deleteById(@PathVariable long userId) {
        return userClient.deleteById(userId);
    }
}
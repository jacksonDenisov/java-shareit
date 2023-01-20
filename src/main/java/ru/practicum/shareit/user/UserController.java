package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping(path = "/users")
public class UserController {

    private final UserService service;

    @Autowired
    public UserController(UserService service) {
        this.service = service;
    }


    @PostMapping
    protected User create(@Valid @RequestBody UserDto userDto) {
        return service.create(userDto);
    }

    @PatchMapping("/{userId}")
    protected User update(@RequestBody UserDto userDto, @PathVariable long userId) {
        return service.update(userDto, userId);
    }

    @GetMapping("/{userId}")
    protected User findById(@PathVariable long userId) {
        return service.findById(userId);
    }

    @GetMapping
    protected List<User> findAll() {
        return service.findAll();
    }

    @DeleteMapping("/{userId}")
    protected void delete(@PathVariable long userId) {
        service.delete(userId);
    }
}

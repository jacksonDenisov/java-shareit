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

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping
    protected User create(@Valid @RequestBody UserDto userDto) {
        return userService.create(userDto);
    }

    @PatchMapping("/{userId}")
    protected User update(@RequestBody UserDto userDto, @PathVariable long userId) {
        return userService.update(userDto, userId);
    }

    @GetMapping("/{userId}")
    protected User findById(@PathVariable long userId) {
        return userService.findById(userId);
    }

    @GetMapping
    protected List<User> findAll() {
        return userService.findAll();
    }

    @DeleteMapping("/{userId}")
    protected void delete(@PathVariable long userId) {
        userService.delete(userId);
    }
}

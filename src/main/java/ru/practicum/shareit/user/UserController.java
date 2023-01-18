package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
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
    protected User create(@Valid @RequestBody User user) {
        return service.create(user);
    }

    @PatchMapping("/{id}")
    protected User update(@RequestBody User user, @PathVariable long id) {
        return service.update(user, id);
    }

    @GetMapping("/{id}")
    protected User findById(@PathVariable long id) {
        return service.findById(id);
    }

    @GetMapping
    protected List<User> findAll() {
        return service.findAll();
    }

    @DeleteMapping("/{id}")
    protected void delete(@PathVariable long id) {
        service.delete(id);
    }

}

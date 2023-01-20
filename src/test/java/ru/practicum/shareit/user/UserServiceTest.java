package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.reposiry.UserRepositoryInMemoryImpl;
import ru.practicum.shareit.utils.exeptions.NotFoundException;
import ru.practicum.shareit.utils.exeptions.ValidationException;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {

    private UserService userService;
    private static UserDto userDto1;
    private static UserDto userDto2;

    @BeforeAll
    static void setUp() {
        userDto1 = new UserDto("name1", "email1@gmail.com");
        userDto2 = new UserDto("name2", "email2@gmail.com");
    }

    @BeforeEach
    public void setup() {
        this.userService = new UserService(new UserRepositoryInMemoryImpl());
    }

    @Test
    void shouldCreateTwoUsersWhenEmailsIsUnique() {
        userService.create(userDto1);
        User user2 = userService.create(userDto2);
        assertNotNull(user2);
    }

    @Test
    void shouldThrowValidationExceptionWhenEmailIsNotUnique() {
        userService.create(userDto1);
        assertThrows(ValidationException.class, () -> {
            userService.create(userDto1);
        });
    }

    @Test
    void shouldCreateUserAgainAfterDelete() {
        userService.create(userDto1);
        assertThrows(ValidationException.class, () -> {
            userService.create(userDto1);
        });
       userService.delete(1);
       User user = userService.create(userDto1);
       assertEquals(user.getEmail(), userDto1.getEmail());
    }

    @Test
    void shouldUpdateUser() {
        userService.create(userDto1);
        assertEquals("name1", userService.findById(1l).getName());
        assertEquals("email1@gmail.com", userService.findById(1l).getEmail());
        userService.update(userDto2, 1);
        assertEquals("name2", userService.findById(1l).getName());
        assertEquals("email2@gmail.com", userService.findById(1l).getEmail());
    }

    @Test
    void shouldNotUpdateUserIfEmailIsNotUnique() {
        userService.create(userDto1);
        assertEquals("name1", userService.findById(1l).getName());
        assertEquals("email1@gmail.com", userService.findById(1l).getEmail());
        assertThrows(ValidationException.class, () -> {
            userService.update(userDto1, 1);
        });
        assertEquals(userService.findById(1l).getName(), "name1");
    }

    @Test
    void shouldThrowNotFoundExceptionWhenTryToUpdateNonExistentUser() {
        assertThrows(NotFoundException.class, () -> {
            userService.update(userDto1,9999l);
        });
    }

    @Test
    void shouldThrowNotFoundExceptionWhenTryToDeleteNonExistentUser() {
        userService.create(userDto1);
        userService.delete(1);
        assertThrows(NotFoundException.class, () -> {
            userService.delete(999);
        });
    }

    @Test
    void shouldThrowNotFoundExceptionWhenTryToGetNonExistentUser() {
        userService.create(userDto1);
        userService.findById(1l);
        assertThrows(NotFoundException.class, () -> {
            userService.findById(999l);
        });
    }

    @Test
    void shouldReturnTrueWhenUserExistAndFalseWhenNotExist() {
        assertFalse(userService.isUserExist(1));
        userService.create(userDto1);
        assertTrue(userService.isUserExist(1));
    }

    @Test
    void shouldReturnCorrectListOfUsers() {
        assertEquals(userService.findAll().size(), 0);
        userService.create(userDto1);
        userService.create(userDto2);
        assertEquals(userService.findAll().size(), 2);
    }
}

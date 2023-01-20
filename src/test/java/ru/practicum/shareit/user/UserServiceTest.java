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
        userService.delete(1L);
        User user = userService.create(userDto1);
        assertEquals(user.getEmail(), userDto1.getEmail());
    }

    @Test
    void shouldUpdateUser() {
        userService.create(userDto1);
        assertEquals("name1", userService.findById(1L).getName());
        assertEquals("email1@gmail.com", userService.findById(1L).getEmail());
        userService.update(userDto2, 1L);
        assertEquals("name2", userService.findById(1L).getName());
        assertEquals("email2@gmail.com", userService.findById(1L).getEmail());
    }

    @Test
    void shouldNotUpdateUserIfEmailIsNotUnique() {
        userService.create(userDto1);
        assertEquals("name1", userService.findById(1L).getName());
        assertEquals("email1@gmail.com", userService.findById(1L).getEmail());
        assertThrows(ValidationException.class, () -> {
            userService.update(userDto1, 1L);
        });
        assertEquals(userService.findById(1L).getName(), "name1");
    }

    @Test
    void shouldThrowNotFoundExceptionWhenTryToUpdateNonExistentUser() {
        assertThrows(NotFoundException.class, () -> {
            userService.update(userDto1, 9999L);
        });
    }

    @Test
    void shouldThrowNotFoundExceptionWhenTryToDeleteNonExistentUser() {
        userService.create(userDto1);
        userService.delete(1L);
        assertThrows(NotFoundException.class, () -> {
            userService.delete(999L);
        });
    }

    @Test
    void shouldThrowNotFoundExceptionWhenTryToGetNonExistentUser() {
        userService.create(userDto1);
        userService.findById(1L);
        assertThrows(NotFoundException.class, () -> {
            userService.findById(999L);
        });
    }

    @Test
    void shouldReturnTrueWhenUserExistAndFalseWhenNotExist() {
        assertFalse(userService.isUserExist(1L));
        userService.create(userDto1);
        assertTrue(userService.isUserExist(1L));
    }

    @Test
    void shouldReturnCorrectListOfUsers() {
        assertEquals(userService.findAll().size(), 0);
        userService.create(userDto1);
        userService.create(userDto2);
        assertEquals(userService.findAll().size(), 2);
    }
}

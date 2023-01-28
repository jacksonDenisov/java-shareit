package ru.practicum.shareit.in_memory_impl;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.in_memory_impl.user.UserDtoInMemory;
import ru.practicum.shareit.in_memory_impl.user.UserForInMemoryImpl;
import ru.practicum.shareit.in_memory_impl.user.UserServiceInMemoryInMemoryImpl;
import ru.practicum.shareit.utils.exeptions.NotFoundException;
import ru.practicum.shareit.utils.exeptions.ValidationException;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

@SpringBootTest
@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
public class UserForInMemoryServiceServiceInMemoryImplTest {

    @Autowired
    private UserServiceInMemoryInMemoryImpl userServiceInMemoryImpl;
    private static UserDtoInMemory userDtoInMemory1;
    private static UserDtoInMemory userDtoInMemory2;

    @BeforeAll
    static void setUp() {
        userDtoInMemory1 = new UserDtoInMemory("name1", "email1@gmail.com");
        userDtoInMemory2 = new UserDtoInMemory("name2", "email2@gmail.com");
    }


    @Test
    void shouldCreateTwoUsersWhenEmailsIsUnique() {
        userServiceInMemoryImpl.create(userDtoInMemory1);
        UserForInMemoryImpl userForInMemoryImpl2 = userServiceInMemoryImpl.create(userDtoInMemory2);
        assertNotNull(userForInMemoryImpl2);
    }

    @Test
    void shouldThrowValidationExceptionWhenEmailIsNotUnique() {
        userServiceInMemoryImpl.create(userDtoInMemory1);
        assertThrows(ValidationException.class, () -> {
            userServiceInMemoryImpl.create(userDtoInMemory1);
        });
    }

    @Test
    void shouldCreateUserAgainAfterDelete() {
        userServiceInMemoryImpl.create(userDtoInMemory1);
        assertThrows(ValidationException.class, () -> {
            userServiceInMemoryImpl.create(userDtoInMemory1);
        });
        userServiceInMemoryImpl.delete(1L);
        UserForInMemoryImpl userForInMemoryImpl = userServiceInMemoryImpl.create(userDtoInMemory1);
        assertEquals(userForInMemoryImpl.getEmail(), userDtoInMemory1.getEmail());
    }

    @Test
    void shouldUpdateUser() {
        userServiceInMemoryImpl.create(userDtoInMemory1);
        assertEquals("name1", userServiceInMemoryImpl.findById(1L).getName());
        assertEquals("email1@gmail.com", userServiceInMemoryImpl.findById(1L).getEmail());
        userServiceInMemoryImpl.update(userDtoInMemory2, 1L);
        assertEquals("name2", userServiceInMemoryImpl.findById(1L).getName());
        assertEquals("email2@gmail.com", userServiceInMemoryImpl.findById(1L).getEmail());
    }

    @Test
    void shouldNotUpdateUserIfEmailIsNotUnique() {
        userServiceInMemoryImpl.create(userDtoInMemory1);
        assertEquals("name1", userServiceInMemoryImpl.findById(1L).getName());
        assertEquals("email1@gmail.com", userServiceInMemoryImpl.findById(1L).getEmail());
        assertThrows(ValidationException.class, () -> {
            userServiceInMemoryImpl.update(userDtoInMemory1, 1L);
        });
        assertEquals(userServiceInMemoryImpl.findById(1L).getName(), "name1");
    }

    @Test
    void shouldThrowNotFoundExceptionWhenTryToUpdateNonExistentUser() {
        assertThrows(NotFoundException.class, () -> {
            userServiceInMemoryImpl.update(userDtoInMemory1, 9999L);
        });
    }

    @Test
    void shouldThrowNotFoundExceptionWhenTryToDeleteNonExistentUser() {
        userServiceInMemoryImpl.create(userDtoInMemory1);
        userServiceInMemoryImpl.delete(1L);
        assertThrows(NotFoundException.class, () -> {
            userServiceInMemoryImpl.delete(999L);
        });
    }

    @Test
    void shouldThrowNotFoundExceptionWhenTryToGetNonExistentUser() {
        userServiceInMemoryImpl.create(userDtoInMemory1);
        userServiceInMemoryImpl.findById(1L);
        assertThrows(NotFoundException.class, () -> {
            userServiceInMemoryImpl.findById(999L);
        });
    }

    @Test
    void shouldReturnTrueWhenUserExistAndFalseWhenNotExist() {
        assertFalse(userServiceInMemoryImpl.isUserExist(1L));
        userServiceInMemoryImpl.create(userDtoInMemory1);
        assertTrue(userServiceInMemoryImpl.isUserExist(1L));
    }

    @Test
    void shouldReturnCorrectListOfUsers() {
        assertEquals(userServiceInMemoryImpl.findAll().size(), 0);
        userServiceInMemoryImpl.create(userDtoInMemory1);
        userServiceInMemoryImpl.create(userDtoInMemory2);
        assertEquals(userServiceInMemoryImpl.findAll().size(), 2);
    }
}

package ru.practicum.shareit.in_memory_tests.user;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import ru.practicum.shareit.in_memory_impl.user.UserDtoInMemory;
import ru.practicum.shareit.in_memory_impl.user.UserMapperForInMemoryImpl;
import ru.practicum.shareit.in_memory_impl.user.UserForInMemoryImpl;

public class UserForInMemoryServiceMapperForInMemoryImplTest {

    private static String name;
    private static String email;
    private static long id;


    @BeforeAll
    static void setUp() {
        name = "name";
        email = "test@email.com";
        id = 1;
    }


    @Test
    void shouldReturnCorrectUser() {
        UserDtoInMemory userDtoInMemory = new UserDtoInMemory(name, email);
        UserForInMemoryImpl userForInMemoryImpl = UserMapperForInMemoryImpl.toUser(userDtoInMemory, id);
        assertEquals(userForInMemoryImpl.getName(), name);
        assertEquals(userForInMemoryImpl.getEmail(), email);
        assertEquals(userForInMemoryImpl.getId(), id);
    }

    @Test
    void shouldReturnCorrectUserDto() {
        UserForInMemoryImpl userForInMemoryImpl = new UserForInMemoryImpl(id, name, email);
        UserDtoInMemory userDtoInMemory = UserMapperForInMemoryImpl.toUserDto(userForInMemoryImpl);
        assertEquals(userDtoInMemory.getName(), name);
        assertEquals(userDtoInMemory.getEmail(), email);
    }
}

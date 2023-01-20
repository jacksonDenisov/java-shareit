package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;

public class UserMapperTest {

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
        UserDto userDto = new UserDto(name, email);
        User user = UserMapper.toUser(userDto, id);
        assertEquals(user.getName(), name);
        assertEquals(user.getEmail(), email);
        assertEquals(user.getId(), id);
    }

    @Test
    void shouldReturnCorrectUserDto() {
        User user = new User(id, name, email);
        UserDto userDto = UserMapper.toUserDto(user);
        assertEquals(userDto.getName(), name);
        assertEquals(userDto.getEmail(), email);
    }
}

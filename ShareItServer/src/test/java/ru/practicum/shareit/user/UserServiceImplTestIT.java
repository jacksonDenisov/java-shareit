package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserServiceImplTestIT {

    @Autowired
    private UserServiceImpl userService;

    private static String userName;
    private static String userName2;
    private static String userEmail;
    private static String userEmail2;


    @BeforeAll
    static void setup() {
        userName = "name";
        userName2 = "name2";
        userEmail = "user@user.com";
        userEmail2 = "user2@user.com";
    }

    @Test
    void shouldReturnCorrectUserDtoWhenCreateUser() {
        UserDto userDto = new UserDto();
        userDto.setName(userName);
        userDto.setEmail(userEmail);
        UserDto result = userService.create(userDto);
        assertEquals(result.getName(), userName);
        assertEquals(result.getEmail(), userEmail);
    }

    @Test
    void shouldReturnCorrectUpdatedUserDtoWhenUpdateUser() {
        UserDto userDto = new UserDto();
        userDto.setName(userName);
        userDto.setEmail(userEmail);
        UserDto result = userService.create(userDto);
        assertEquals(result.getName(), userName);
        long id = result.getId();
        userDto.setName(userName2);
        userDto.setEmail(userEmail2);
        UserDto result2 = userService.update(userDto, id);
        assertEquals(result2.getName(), userName2);
        assertEquals(result2.getEmail(), userEmail2);
    }

    @Test
    void shouldReturnCorrectUserListWhenCallFindAll() {
        UserDto userDto = new UserDto();
        userDto.setName(userName);
        userDto.setEmail(userEmail);
        assertEquals(userService.findAll().size(), 0);
        userService.create(userDto);
        assertEquals(userService.findAll().size(), 1);
        assertEquals(userService.findAll().get(0).getName(), userName);
    }

    @Test
    void shouldRemoveUserWhenCallDelete() {
        UserDto userDto = new UserDto();
        userDto.setName(userName);
        userDto.setEmail(userEmail);
        userService.create(userDto);
        assertEquals(userService.findAll().size(), 1);
        userService.deleteById(1L);
        assertEquals(userService.findAll().size(), 0);
    }

    @Test
    void shouldFindUserById() {
        UserDto userDto = new UserDto();
        userDto.setName(userName);
        userDto.setEmail(userEmail);
        userService.create(userDto);
        UserDto result = userService.findById(1L);
        assertEquals(result.getId(), 1L);
        assertEquals(result.getName(), userDto.getName());
        assertEquals(result.getEmail(), userDto.getEmail());
    }

}

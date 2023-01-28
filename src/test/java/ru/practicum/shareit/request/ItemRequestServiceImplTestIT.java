package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ItemRequestServiceImplTestIT {

    @Autowired
    private ItemRequestServiceImpl itemRequestService;

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldReturnCorrectItemRequestDtoWhenCreateNew() {
        User user = new User();
        user.setName("name");
        user.setEmail("user@user.com");
        User userSaved = userRepository.save(user);
        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setDescription("Описание для запроса вещи");
        ItemRequestDto result = itemRequestService.create(userSaved.getId(), itemRequestDto);
        assertEquals(result.getId(), 1L);
        assertEquals(result.getDescription(), "Описание для запроса вещи");
    }

    @Test
    void shouldThrowNoSuchElementExceptionWhenCreateWithWrongUser() {
        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setDescription("Описание для запроса вещи");
        assertThrows(NoSuchElementException.class, () -> {
            itemRequestService.create(9999L, itemRequestDto);
        });
    }
}
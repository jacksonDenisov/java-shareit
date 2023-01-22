package ru.practicum.shareit.user.reposiry;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserRepository {

    User save(UserDto userDto);

    User update(UserDto userDto, long id);

    User findById(Long id);

    List<User> findAll();

    void delete(long id);

    boolean isUserExist(long id);

    boolean isEmailUnique(String email);
}

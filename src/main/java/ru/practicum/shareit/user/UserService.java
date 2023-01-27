package ru.practicum.shareit.user;

import java.util.List;

public interface UserService {

    List<UserDto> findAll();

    UserDto create(UserDto userDto);

    UserDto update(UserDto userDto, long userID);

    UserDto findById(long userId);

    void deleteById(long userId);
}

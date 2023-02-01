package ru.practicum.shareit.user;

import java.util.List;

public interface UserService {

    List<UserDto> findAll();

    UserDto create(UserDto userDto);

    UserDto update(UserDto userDto, long userID);

    UserDto findById(long userId);

    boolean deleteById(long userId);
}
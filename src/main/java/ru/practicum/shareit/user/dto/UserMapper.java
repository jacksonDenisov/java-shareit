package ru.practicum.shareit.user.dto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.user.model.User;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserMapper {

    public static UserDto toUserDto(User user) {
        return new UserDto(
                user.getName(),
                user.getEmail());
    }

    public static User toUser(UserDto userDto, long userId) {
        return new User(
                userId,
                userDto.getName(),
                userDto.getEmail());
    }
}

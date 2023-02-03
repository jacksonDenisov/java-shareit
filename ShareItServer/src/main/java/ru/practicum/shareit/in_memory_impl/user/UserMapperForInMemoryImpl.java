package ru.practicum.shareit.in_memory_impl.user;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserMapperForInMemoryImpl {

    public static UserDtoInMemory toUserDto(UserForInMemoryImpl userForInMemoryImpl) {
        return new UserDtoInMemory(
                userForInMemoryImpl.getName(),
                userForInMemoryImpl.getEmail());
    }

    public static UserForInMemoryImpl toUser(UserDtoInMemory userDtoInMemory, long userId) {
        return new UserForInMemoryImpl(
                userId,
                userDtoInMemory.getName(),
                userDtoInMemory.getEmail());
    }
}

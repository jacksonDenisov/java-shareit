package ru.practicum.shareit.in_memory_impl.user;

import java.util.List;

public interface UserServiceInMemory {

    UserForInMemoryImpl create(UserDtoInMemory userDtoInMemory);

    UserForInMemoryImpl update(UserDtoInMemory userDtoInMemory, long id);

    UserForInMemoryImpl findById(Long id);

    List<UserForInMemoryImpl> findAll();

    void delete(long id);
}

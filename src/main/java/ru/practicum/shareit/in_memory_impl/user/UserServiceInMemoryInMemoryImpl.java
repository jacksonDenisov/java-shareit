package ru.practicum.shareit.in_memory_impl.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.utils.exeptions.NotFoundException;
import ru.practicum.shareit.utils.exeptions.ValidationException;

import java.util.List;

@Service
@Slf4j
@Qualifier("UserServiceInMemory")
public class UserServiceInMemoryInMemoryImpl implements UserServiceInMemory {

    private final UserRepositoryInMemory userRepositoryInMemory;

    @Autowired
    public UserServiceInMemoryInMemoryImpl(UserRepositoryInMemory userRepositoryInMemory) {
        this.userRepositoryInMemory = userRepositoryInMemory;
    }


    @Override
    public UserForInMemoryImpl create(UserDtoInMemory userDtoInMemory) {
        log.info("Создаем нового пользователя {}", userDtoInMemory);
        if (!userRepositoryInMemory.isEmailUnique(userDtoInMemory.getEmail())) {
            throw new ValidationException("Такой пользователь уже существует!");
        }
        return userRepositoryInMemory.save(userDtoInMemory);
    }

    @Override
    public UserForInMemoryImpl update(UserDtoInMemory userDtoInMemory, long id) {
        log.info("Обновляем пользователя с id = {}.", id);
        if (!userRepositoryInMemory.isUserExist(id)) {
            throw new NotFoundException("Такого пользователя не существует!");
        }
        return userRepositoryInMemory.update(userDtoInMemory, id);
    }

    @Override
    public UserForInMemoryImpl findById(Long id) {
        log.info("Возвращаем пользователя с id = {}.", id);
        if (!userRepositoryInMemory.isUserExist(id)) {
            throw new NotFoundException("Пользователь не найден!");
        }
        return userRepositoryInMemory.findById(id);
    }

    @Override
    public List<UserForInMemoryImpl> findAll() {
        log.info("Возвращаем список всех пользователей.");
        return userRepositoryInMemory.findAll();
    }

    @Override
    public void delete(long id) {
        log.info("Удаляем пользователя с id = {}.", id);
        if (!userRepositoryInMemory.isUserExist(id)) {
            throw new NotFoundException("Пользователь не найден!");
        }
        userRepositoryInMemory.delete(id);
    }

    public boolean isUserExist(long id) {
        return userRepositoryInMemory.isUserExist(id);
    }
}

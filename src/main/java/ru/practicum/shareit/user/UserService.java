package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.reposiry.UserRepository;
import ru.practicum.shareit.user.reposiry.UserRepositoryInMemoryImpl;
import ru.practicum.shareit.utils.exeptions.NotFoundException;
import ru.practicum.shareit.utils.exeptions.ValidationException;

import java.util.List;

@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepositoryInMemoryImpl userRepositoryInMemoryImpl) {
        this.userRepository = userRepositoryInMemoryImpl;
    }


    public User create(UserDto userDto) {
        log.info("Создаем нового пользователя {}", userDto);
        if (!userRepository.isEmailUnique(userDto.getEmail())) {
            throw new ValidationException("Такой пользователь уже существует!");
        }
        return userRepository.save(userDto);
    }

    public User update(UserDto userDto, long id) {
        log.info("Обновляем пользователя с id = {}.", id);
        if (!userRepository.isUserExist(id)) {
            throw new NotFoundException("Такого пользователя не существует!");
        }
        return userRepository.update(userDto, id);
    }

    public User findById(Long id) {
        log.info("Возвращаем пользователя с id = {}.", id);
        if (!userRepository.isUserExist(id)) {
            throw new NotFoundException("Пользователь не найден!");
        }
        return userRepository.findById(id);
    }

    public List<User> findAll() {
        log.info("Возвращаем список всех пользователей.");
        return userRepository.findAll();
    }

    public void delete(long id) {
        log.info("Удаляем пользователя с id = {}.", id);
        if (!userRepository.isUserExist(id)) {
            throw new NotFoundException("Пользователь не найден!");
        }
        userRepository.delete(id);
    }

    public boolean isUserExist(long id) {
        return userRepository.isUserExist(id);
    }
}

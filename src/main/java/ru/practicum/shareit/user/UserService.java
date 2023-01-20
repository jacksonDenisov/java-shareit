package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.reposiry.UserRepository;
import ru.practicum.shareit.utils.exeptions.NotFoundException;
import ru.practicum.shareit.utils.exeptions.ValidationException;

import java.util.List;

@Service
@Slf4j
public class UserService {

    private final UserRepository repository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.repository = userRepository;
    }


    public User create(UserDto userDto) {
        log.info("Создаем нового пользователя {}", userDto);
        if (!repository.isEmailUnique(userDto.getEmail())) {
            throw new ValidationException("Такой пользователь уже существует!");
        }
        return repository.save(userDto);
    }

    public User update(UserDto userDto, long id) {
        log.info("Обновляем пользователя с id = {}.", id);
        if (!repository.isUserExist(id)) {
            throw new NotFoundException("Такого пользователя не существует!");
        }
        return repository.update(userDto, id);
    }

    public User findById(Long id) {
        log.info("Возвращаем пользователя с id = {}.", id);
        if (!repository.isUserExist(id)) {
            throw new NotFoundException("Пользователь не найден!");
        }
        return repository.findById(id);
    }

    public List<User> findAll() {
        log.info("Возвращаем список всех пользователей.");
        return repository.findAll();
    }

    public void delete(long id) {
        log.info("Удаляем пользователя с id = {}.", id);
        if (!repository.isUserExist(id)) {
            throw new NotFoundException("Пользователь не найден!");
        }
        repository.delete(id);
    }

    public boolean isUserExist(long id) {
        return repository.isUserExist(id);
    }
}

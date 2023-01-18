package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.reposiry.UserRepository;

import java.util.List;

@Service
@Slf4j
public class UserService {

    UserRepository repository;

    @Autowired
    public UserService(UserRepository userRepository){
        this.repository = userRepository;
    }


    public User create(User user) {
        log.info("Создаем нового пользователя " + user);
        return repository.save(user);
    }

    public User update(User user, long id) {
        log.info("Обновляем пользователя " + user);
        return repository.update(user, id);
    }

    public User findById(Long id) {
        log.info("Возвращаем пользователя с id " + id);
        return repository.findById(id);
    }

    public List<User> findAll() {
        log.info("Возвращаем список всех пользователей.");
        return repository.findAll();
    }

    public void delete(long id) {
        log.info("Удаляем пользователя с id = {}.", id);
        repository.delete(id);
    }
}

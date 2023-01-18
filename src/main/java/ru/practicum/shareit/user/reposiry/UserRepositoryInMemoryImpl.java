package ru.practicum.shareit.user.reposiry;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exeptions.NotFoundException;
import ru.practicum.shareit.exeptions.ValidationException;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@Repository
@Slf4j
public class UserRepositoryInMemoryImpl implements UserRepository {

    private final Map<Long, User> users = new HashMap<>();
    private final Set<String> uniqueEmails = new HashSet<>();
    private static long id = 0;


    @Override
    public User save(User user) {
        if (uniqueEmails.contains(user.getEmail())) {
            throw new ValidationException("Такой пользователь уже существует!");
        }
        user.setId(++id);
        users.put(id, user);
        uniqueEmails.add(user.getEmail());
        log.info("Пользователь {} успешно добавлен.", id);
        return user;
    }


    @Override
    public User update(User user, long id) {
        if (!users.containsKey(id)) {
            throw new NotFoundException("Такого пользователя не существует!");
        }
        User updatedUser = users.get(id);
        if (user.getEmail() != null ){
            updateUniqueEmail(id, user.getEmail());
            updatedUser.setEmail(user.getEmail());
        }
        if (user.getName() != null){
            updatedUser.setName(user.getName());
        }
        users.put(id, updatedUser);
        log.info("Пользователь {} успешно обновлен", id);
        return updatedUser;
    }

    @Override
    public User findById(Long id) {
        if (users.containsKey(id)) {
            return users.get(id);
        } else {
            throw new NotFoundException("Пользователь не найден!");
        }
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public void delete(long id) {
        if (users.containsKey(id)) {
            uniqueEmails.remove(users.get(id).getEmail());
            users.remove(id);
        } else {
            throw new NotFoundException("Пользователь не найден!");
        }
    }

    private void updateUniqueEmail(long id, String email){
        if (uniqueEmails.contains(email)){
            throw new ValidationException("Такой email уже существует!");
        }
        uniqueEmails.remove(users.get(id).getEmail());
        uniqueEmails.add(email);
    }
}

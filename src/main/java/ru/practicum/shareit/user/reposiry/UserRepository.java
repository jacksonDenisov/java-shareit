package ru.practicum.shareit.user.reposiry;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserRepository {

    User save(User user);

    User update(User user, long id);

    User findById(Long id);

    List<User> findAll();

    void delete(long id);
}

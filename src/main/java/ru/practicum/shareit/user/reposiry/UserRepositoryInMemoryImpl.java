package ru.practicum.shareit.user.reposiry;


import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.utils.exeptions.ValidationException;

import java.util.*;

@Repository
public class UserRepositoryInMemoryImpl implements UserRepository {

    private final Map<Long, User> users;
    private final Set<String> uniqueEmails;
    private long id;

    public UserRepositoryInMemoryImpl() {
        users = new HashMap<>();
        uniqueEmails = new HashSet<>();
        id = 0;
    }

    @Override
    public User save(UserDto userDto) {
        User user = UserMapper.toUser(userDto, ++id);
        users.put(id, user);
        uniqueEmails.add(user.getEmail());
        return user;
    }


    @Override
    public User update(UserDto userDto, long id) {
        User updatedUser = users.get(id);
        if (userDto.getEmail() != null) {
            updateUniqueEmail(id, userDto.getEmail());
            updatedUser.setEmail(userDto.getEmail());
        }
        if (userDto.getName() != null) {
            updatedUser.setName(userDto.getName());
        }
        users.put(id, updatedUser);
        return updatedUser;
    }

    @Override
    public User findById(Long id) {
        return users.get(id);
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public void delete(long id) {
        uniqueEmails.remove(users.get(id).getEmail());
        users.remove(id);
    }

    @Override
    public boolean isUserExist(long id) {
        return users.containsKey(id);
    }

    @Override
    public boolean isEmailUnique(String email) {
        return !uniqueEmails.contains(email);
    }

    private void updateUniqueEmail(long id, String email) {
        if (!isEmailUnique(email)) {
            throw new ValidationException("Такой email уже существует!");
        }
        uniqueEmails.remove(users.get(id).getEmail());
        uniqueEmails.add(email);
    }
}

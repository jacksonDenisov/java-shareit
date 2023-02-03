package ru.practicum.shareit.in_memory_impl.user;


import org.springframework.stereotype.Repository;
import ru.practicum.shareit.utils.exeptions.ValidationException;

import java.util.*;

@Repository
public class UserRepositoryInMemory {

    private final Map<Long, UserForInMemoryImpl> users;
    private final Set<String> uniqueEmails;
    private long id;

    public UserRepositoryInMemory() {
        users = new HashMap<>();
        uniqueEmails = new HashSet<>();
        id = 0;
    }

    public UserForInMemoryImpl save(UserDtoInMemory userDtoInMemory) {
        UserForInMemoryImpl userForInMemoryImpl = UserMapperForInMemoryImpl.toUser(userDtoInMemory, ++id);
        users.put(id, userForInMemoryImpl);
        uniqueEmails.add(userForInMemoryImpl.getEmail());
        return userForInMemoryImpl;
    }


    public UserForInMemoryImpl update(UserDtoInMemory userDtoInMemory, long id) {
        UserForInMemoryImpl updatedUserForInMemoryImpl = users.get(id);
        if (userDtoInMemory.getEmail() != null) {
            updateUniqueEmail(id, userDtoInMemory.getEmail());
            updatedUserForInMemoryImpl.setEmail(userDtoInMemory.getEmail());
        }
        if (userDtoInMemory.getName() != null) {
            updatedUserForInMemoryImpl.setName(userDtoInMemory.getName());
        }
        users.put(id, updatedUserForInMemoryImpl);
        return updatedUserForInMemoryImpl;
    }

    public UserForInMemoryImpl findById(Long id) {
        return users.get(id);
    }

    public List<UserForInMemoryImpl> findAll() {
        return new ArrayList<>(users.values());
    }

    public void delete(long id) {
        uniqueEmails.remove(users.get(id).getEmail());
        users.remove(id);
    }

    public boolean isUserExist(long id) {
        return users.containsKey(id);
    }

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

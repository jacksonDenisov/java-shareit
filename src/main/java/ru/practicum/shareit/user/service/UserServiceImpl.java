package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.reposiry.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;


    @Override
    @Transactional
    public UserDto create(UserDto userDto) {
        User user = userRepository.save(UserMapper.toUser(userDto));
        return UserMapper.toUserDto(user);
    }

    @Override
    @Transactional
    public UserDto update(UserDto userDto, long userID) {
        if (userDto.getName() != null && userDto.getEmail() != null) {
            userRepository.updateNameAndEmailById(userDto.getName(), userDto.getEmail(), userID);
        } else if (userDto.getName() != null) {
            userRepository.updateNameById(userDto.getName(), userID);
        } else if (userDto.getEmail() != null) {
            userRepository.updateEmailById(userDto.getEmail(), userID);
        }
        return findById(userID);
    }

    @Override
    public UserDto findById(long userId) {
        Optional<User> user = userRepository.findById(userId);
        return UserMapper.toUserDto(user.get());
    }

    @Override
    public List<UserDto> findAll() {
        List<User> users = userRepository.findAll();
        return UserMapper.toUserDto(users);
    }

    @Override
    @Transactional
    public void deleteById(long userId) {
        userRepository.deleteById(userId);
    }

}

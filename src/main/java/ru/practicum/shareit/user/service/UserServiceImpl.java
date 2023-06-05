package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User addUser(User user) {
        return UserMapper.toUser(userRepository.addUser(UserMapper.toUserDto(user)));
    }

    @Override
    public User updateUser(User updateUser, long userId) {
        Optional<User> userById = getUserById(userId);
        if (userById.isEmpty())
            throw new NotFoundException(String.format("юзера с таким id %d нет", userId));
        updateUser = UserMapper.toUser(userRepository.updateUser(UserMapper.toUserDto(updateUserNameAndEmail(updateUser, userById.get()))));
        return updateUser;
    }

    @Override
    public Optional<User> getUserById(long id) {
        Optional<UserDto> userDto = userRepository.getUserById(id);
        if (userDto.isEmpty())
            throw new NotFoundException("нет такого юзера");
        return Optional.of(UserMapper.toUser(userDto.get()));
    }

    @Override
    public void deleteUserById(long userId) {
        userRepository.deleteUser(userId);
    }

    @Override
    public List<User> getAll() {
        return userRepository
                .getAll()
                .stream()
                .map(UserMapper::toUser)
                .collect(Collectors.toList());
    }

    private User updateUserNameAndEmail(User newUser, User oldUser) {
        if (newUser.getEmail() != null && !newUser.getEmail().equals(oldUser.getEmail()))
            oldUser.setEmail(newUser.getEmail());
        if (newUser.getName() != null && !newUser.getName().equals(oldUser.getName()))
            oldUser.setName(newUser.getName());
        return oldUser;
    }
}

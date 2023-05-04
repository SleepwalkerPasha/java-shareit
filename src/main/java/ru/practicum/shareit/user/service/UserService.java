package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    User addUser(User user);

    User updateUser(User info, long userId);

    Optional<User> getUserById(long id);

    void deleteUserById(long userId);

    List<User> getAll();
}

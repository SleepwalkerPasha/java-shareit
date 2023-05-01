package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    UserDto addUser(UserDto user);

    UserDto updateUser(UserDto updatedUser, long userId);

    Optional<UserDto> getUserById(long userId);

    void deleteUser(long userId);

    List<UserDto> getAll();

    UserDto checkForUser(long userId);
}

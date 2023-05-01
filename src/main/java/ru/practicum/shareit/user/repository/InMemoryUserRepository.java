package ru.practicum.shareit.user.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class InMemoryUserRepository implements UserRepository {

    private final HashMap<Long, UserDto> userMap = new HashMap<>();

    private final Set<String> emails = new HashSet<>();

    private static long counter = 0L;

    @Override
    public UserDto addUser(UserDto user) {
        if (emails.contains(user.getEmail()))
            throw new ConflictException("пользователь с данным email уже зарегистрирован");
        user.setId(++counter);
        emails.add(user.getEmail());
        userMap.putIfAbsent(user.getId(), user);
        return user;
    }

    @Override
    public UserDto updateUser(UserDto updatedUser, long userId) {
        UserDto oldUserData = checkForUser(userId);
        if (emails.contains(updatedUser.getEmail()) && !oldUserData.getEmail().equals(updatedUser.getEmail()))
            throw new ConflictException("данный email уже занят другим пользователем");
        userMap.put(userId, updatedUser);
        emails.remove(oldUserData.getEmail());
        emails.add(updatedUser.getEmail());
        return updatedUser;
    }

    @Override
    public Optional<UserDto> getUserById(long id) {
        return Optional.ofNullable(userMap.get(id));
    }

    @Override
    public void deleteUser(long userId) {
        UserDto userDto = checkForUser(userId);
        emails.remove(userDto.getEmail());
        userMap.remove(userId);
    }

    @Override
    public List<UserDto> getAll() {
        return new ArrayList<>(userMap.values());
    }

    @Override
    public UserDto checkForUser(long userId) {
        Optional<UserDto> userById = getUserById(userId);
        if (userById.isEmpty())
            throw new NotFoundException("такого пользователя нет");
        else
            return userById.get();
    }
}
